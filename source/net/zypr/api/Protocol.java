/**
 * 
 * Initial version of this code (c) 2009-2011 Media Tuners LLC with a full license to Pioneer Corporation.
 * 
 * Pioneer Corporation licenses this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 * 
 */


package net.zypr.api;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import java.net.URLEncoder;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.zip.GZIPInputStream;

import net.zypr.api.enums.APIVerbs;
import net.zypr.api.enums.StatusCode;
import net.zypr.api.exceptions.APICommunicationException;
import net.zypr.api.exceptions.APIProtocolException;
import net.zypr.api.exceptions.APIRuntimeException;
import net.zypr.api.utils.Debug;
import net.zypr.api.utils.NumberUtils;
import net.zypr.api.vo.ServiceDetailsVO;
import net.zypr.api.vo.ServiceVO;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HttpContext;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

class Protocol
{
  private String _apiEntity;
  private Hashtable<String, ServiceVO> _services = new Hashtable<String, ServiceVO>();
  private Hashtable<APIVerbs, String> _defaultServices = new Hashtable<APIVerbs, String>();
  private Hashtable<APIVerbs, String> _allServices = new Hashtable<APIVerbs, String>();
  private SocketFactory _socketFactory;
  public static final String SERVICE_NAME_ALL = "all";
  public static final String SERVICE_NAME_DEFAULT = "default";

  public Protocol()
  {
    super();
    initSocketFactory();
    _apiEntity = this.getClass().getCanonicalName().substring(this.getClass().getPackage().getName().length() + 1).toLowerCase();
  }

  public Protocol(boolean runDescribe)
    throws APICommunicationException, APIProtocolException
  {
    this();
    if (runDescribe)
      setServices(describe());
  }

  public Protocol(ServiceVO[] services)
  {
    this();
    setServices(services);
  }

  public void setServices(ServiceVO[] services)
  {
    _services.clear();
    _defaultServices.clear();
    _allServices.clear();
    for (int index = 0; index < services.length; index++)
      {
        String serviceName = services[index].getServiceName();
        if (serviceName.equalsIgnoreCase(SERVICE_NAME_DEFAULT))
          {
            String[] verbs = services[index].getVerbs();
            for (int verbsIndex = 0; verbsIndex < verbs.length; verbsIndex++)
              try
                {
                  _defaultServices.put(APIVerbs.valueOf(_apiEntity, verbs[verbsIndex]), services[index].getVerbParameter(verbs[verbsIndex], "service_name").getValueAsString());
                }
              catch (Exception expection)
                {
                  Debug.displayWarning(this, "Invalid APIVerbs default service : " + _apiEntity + "/" + verbs[verbsIndex]);
                }
          }
        else if (serviceName.equalsIgnoreCase(SERVICE_NAME_ALL))
          {
            String[] verbs = services[index].getVerbs();
            for (int verbsIndex = 0; verbsIndex < verbs.length; verbsIndex++)
              try
                {
                  _allServices.put(APIVerbs.valueOf(_apiEntity, verbs[verbsIndex]), services[index].getVerbParameter(verbs[verbsIndex], "service_name").getValueAsString());
                }
              catch (Exception expection)
                {
                  Debug.displayWarning(this, "Invalid APIVerbs all service : " + _apiEntity + "/" + verbs[verbsIndex]);
                }
          }
        else
          {
            _services.put(serviceName, services[index]);
          }
      }
  }

  public SocketFactory initSocketFactory()
  {
    if (_socketFactory == null)
      _socketFactory = SSLSocketFactory.getSocketFactory();
    return (_socketFactory);
  }

  public String getAPIEntity()
  {
    return (_apiEntity);
  }

  public String getDefaultService(APIVerbs apiVerb, String defaultServiceName)
  {
    String service = getDefaultService(apiVerb);
    return (service == null ? defaultServiceName : service);
  }

  public String getDefaultService(APIVerbs apiVerb)
  {
    if (apiVerb == null)
      return (null);
    return (_defaultServices.get(apiVerb));
  }

  public String getAllService(APIVerbs apiVerb, String defaultServiceName)
  {
    String allService = getAllService(apiVerb);
    return (allService == null ? defaultServiceName : allService);
  }

  public String getAllService(APIVerbs apiVerb)
  {
    if (apiVerb == null)
      return (null);
    return (_allServices.get(apiVerb));
  }

  @Override
  public boolean equals(Object object)
  {
    if (this == object)
      return (true);
    else if (!(object instanceof Protocol))
      return (false);
    final Protocol other = (Protocol) object;
    return (_apiEntity.equals(other._apiEntity));
  }

  @Override
  public int hashCode()
  {
    return (37 + (_apiEntity.hashCode()));
  }

  public String buildURL(APIVerbs apiVerb, Hashtable<String, String> parameters)
  {
    String urlString = API.SERVER_URL + apiVerb.toPath() + "/";
    if (parameters != null)
      {
        boolean first = true;
        for (Enumeration keys = parameters.keys(); keys.hasMoreElements(); )
          {
            String key = (String) keys.nextElement();
            String value = parameters.get(key);
            try
              {
                urlString += (first ? "?" : "&") + URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(value, "UTF-8");
                first = false;
              }
            catch (UnsupportedEncodingException unsupportedEncodingException)
              {
                throw new APIRuntimeException(unsupportedEncodingException);
              }
          }
      }
    return (urlString);
  }

  public JSONObject getJSON(APIVerbs apiVerb, Hashtable<String, String> parameters)
    throws APICommunicationException, APIProtocolException
  {
    return (doGetJSON(buildURL(apiVerb, parameters)));
  }

  public byte[] doGetBytes(String url)
    throws APICommunicationException
  {
    Session.getInstance().addActiveRequestCount();
    long t1 = System.currentTimeMillis();
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    try
      {
        DefaultHttpClient httpclient = getHTTPClient();
        HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
        if (httpResponse.getStatusLine().getStatusCode() != 200)
          throw new APICommunicationException("HTTP Error " + httpResponse.getStatusLine().getStatusCode() + " - " + httpResponse.getStatusLine().getReasonPhrase() + " at " + url);
        HttpEntity httpEntity = httpResponse.getEntity();
        InputStream inputStream = httpEntity.getContent();
        byte[] buffer = new byte[4096];
        int readCount = 0;
        while ((readCount = inputStream.read(buffer)) != -1)
          byteArrayOutputStream.write(buffer, 0, readCount);
        httpclient.getConnectionManager().shutdown();
      }
    catch (IOException ioException)
      {
        throw new APICommunicationException(ioException);
      }
    finally
      {
        Session.getInstance().removeActiveRequestCount();
        long t2 = System.currentTimeMillis();
        Debug.print(url + " : " + t1 + "-" + t2 + "=" + (t2 - t1) + " : " + byteArrayOutputStream.size() + " bytes");
      }
    return (byteArrayOutputStream.toByteArray());
  }

  public byte[] doGetBytes(APIVerbs apiVerb, Hashtable<String, String> parameters)
    throws APICommunicationException
  {
    return (doGetBytes(buildURL(apiVerb, parameters)));
  }

  public JSONObject doFilePost(APIVerbs apiVerb, Hashtable<String, String> urlParameters, Hashtable<String, String> postParameters, File file)
    throws APICommunicationException, APIProtocolException
  {
    try
      {
        return (doStreamPost(apiVerb, urlParameters, postParameters, new FileInputStream(file)));
      }
    catch (IOException ioException)
      {
        throw new APICommunicationException(ioException);
      }
  }

  public JSONObject doStreamPost(APIVerbs apiVerb, Hashtable<String, String> urlParameters, Hashtable<String, String> postParameters, InputStream inputStream)
    throws APICommunicationException, APIProtocolException
  {
    if (!_apiEntity.equalsIgnoreCase("voice"))
      Session.getInstance().addActiveRequestCount();
    long t1 = System.currentTimeMillis();
    JSONObject jsonObject = null;
    JSONParser jsonParser = new JSONParser();
    try
      {
        DefaultHttpClient httpclient = getHTTPClient();
        HttpPost httpPost = new HttpPost(buildURL(apiVerb, urlParameters));
        HttpProtocolParams.setUseExpectContinue(httpPost.getParams(), false);
        MultipartEntity multipartEntity = new MultipartEntity();
        if (postParameters != null)
          for (Enumeration enumeration = postParameters.keys(); enumeration.hasMoreElements(); )
            {
              String key = enumeration.nextElement().toString();
              String value = postParameters.get(key);
              multipartEntity.addPart(key, new StringBody(value));
              Debug.print("HTTP POST : " + key + "=" + value);
            }
        if (inputStream != null)
          {
            InputStreamBody inputStreamBody = new InputStreamBody(inputStream, "binary/octet-stream");
            multipartEntity.addPart("audio", inputStreamBody);
          }
        httpPost.setEntity(multipartEntity);
        HttpResponse httpResponse = httpclient.execute(httpPost);
        if (httpResponse.getStatusLine().getStatusCode() != 200)
          throw new APICommunicationException("HTTP Error " + httpResponse.getStatusLine().getStatusCode() + " - " + httpResponse.getStatusLine().getReasonPhrase());
        HttpEntity httpEntity = httpResponse.getEntity();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpEntity.getContent()));
        jsonObject = (JSONObject) jsonParser.parse(bufferedReader);
        bufferedReader.close();
        httpclient.getConnectionManager().shutdown();
      }
    catch (ParseException parseException)
      {
        throw new APIProtocolException(parseException);
      }
    catch (IOException ioException)
      {
        throw new APICommunicationException(ioException);
      }
    catch (ClassCastException classCastException)
      {
        throw new APIProtocolException(classCastException);
      }
    finally
      {
        if (!_apiEntity.equalsIgnoreCase("voice"))
          Session.getInstance().removeActiveRequestCount();
        long t2 = System.currentTimeMillis();
        Debug.print(buildURL(apiVerb, urlParameters) + " : " + t1 + "-" + t2 + "=" + (t2 - t1) + " : " + jsonObject);
      }
    return (jsonObject);
  }

  public synchronized ServiceVO[] describe()
    throws APIProtocolException, APICommunicationException
  {
    Hashtable<String, String> parameters = buildParameters();
    ServiceVO[] services = null;
    try
      {
        JSONObject jsonObject = getJSON(APIVerbs.DESCRIBE, parameters);
        if (getStatusCode(jsonObject) == StatusCode.SUCCESSFUL)
          {
            JSONArray serviceVerbsJSONArray = getDataKeyedArrayJSON(jsonObject, "service_verbs");
            services = new ServiceVO[serviceVerbsJSONArray.size()];
            for (int index = 0; index < services.length; index++)
              services[index] = new ServiceVO((JSONObject) serviceVerbsJSONArray.get(index));
          }
        else
          {
            throw new APIProtocolException(getStatusMessage(jsonObject));
          }
      }
    catch (ClassCastException classCastException)
      {
        throw new APIProtocolException(classCastException);
      }
    catch (NullPointerException nullPointerException)
      {
        throw new APIProtocolException(nullPointerException);
      }
    return (services);
  }

  public boolean doesVerbExist(String service, APIVerbs apiVerb)
  {
    if (service == null || service.trim().equals(""))
      return (false);
    ServiceVO serviceVO = _services.get(service);
    if (serviceVO == null)
      return (false);
    return (serviceVO.doesVerbExist(apiVerb.getVerb()));
  }

  public ServiceDetailsVO getDefaultServiceDetails(APIVerbs apiVerb, String defaultServiceName)
  {
    String service = getDefaultService(apiVerb, defaultServiceName);
    if (service == null)
      return (null);
    return (getServiceDetails(service));
  }

  public ServiceDetailsVO getDefaultServiceDetails(APIVerbs apiVerb)
  {
    return (getDefaultServiceDetails(apiVerb, null));
  }

  public ServiceDetailsVO getAllServiceDetails(APIVerbs apiVerb, String defaultServiceName)
  {
    String service = getAllService(apiVerb, defaultServiceName);
    if (service == null)
      return (null);
    return (getServiceDetails(service));
  }

  public ServiceDetailsVO getAllServiceDetails(APIVerbs apiVerb)
  {
    return (getAllServiceDetails(apiVerb, null));
  }

  public ServiceDetailsVO getServiceDetails(String service)
  {
    ServiceVO serviceVO = _services.get(service);
    if (serviceVO != null)
      return (serviceVO.getServiceDetails());
    return (null);
  }

  public ServiceVO[] getServices()
  {
    return (_services.values().toArray(new ServiceVO[0]));
  }

  public ServiceVO getService(String service)
  {
    return (_services.get(service));
  }

  public boolean hasAnyServices()
  {
    return (_services.size() > 0);
  }

  public String[] getAllVerbs()
  {
    SortedSet<String> sortedSet = new TreeSet<String>();
    for (Enumeration enumeration = _services.elements(); enumeration.hasMoreElements(); )
      {
        String[] verbs = ((ServiceVO) (enumeration.nextElement())).getVerbs();
        for (int index = 0; index < verbs.length; index++)
          sortedSet.add(verbs[index]);
      }
    return (sortedSet.toArray(new String[0]));
  }

  protected Hashtable<String, String> buildParameters()
  {
    Hashtable<String, String> parameters = new Hashtable<String, String>();
    if (Session.getInstance().getToken() != null)
      parameters.put("token", Session.getInstance().getToken());
    else
      parameters.put("key", API.LICENCE_KEY);
    if (Session.getInstance().getPosition() != null)
      {
        parameters.put("lat", "" + NumberUtils.roundToDecimals(Session.getInstance().getPosition().getLatitude()));
        parameters.put("lng", "" + NumberUtils.roundToDecimals(Session.getInstance().getPosition().getLongitude()));
      }
    return (parameters);
  }

  protected JSONObject getResponseJSON(JSONObject jsonObject)
    throws APIProtocolException
  {
    try
      {
        return ((JSONObject) jsonObject.get("response"));
      }
    catch (Exception exception)
      {
        throw new APIProtocolException(exception);
      }
  }

  protected JSONObject getInformationJSON(JSONObject jsonObject)
    throws APIProtocolException
  {
    try
      {
        return ((JSONObject) getResponseJSON(jsonObject).get("information"));
      }
    catch (Exception exception)
      {
        throw new APIProtocolException(exception);
      }
  }

  protected JSONArray getDataArrayJSON(JSONObject jsonObject)
    throws APIProtocolException
  {
    try
      {
        return ((JSONArray) getResponseJSON(jsonObject).get("data"));
      }
    catch (Exception exception)
      {
        throw new APIProtocolException(exception);
      }
  }

  protected JSONObject getDataKeyedObjectJSON(JSONObject jsonObject, String objectKey)
    throws APIProtocolException
  {
    return (findJSONObjectInArray(getDataArrayJSON(jsonObject), objectKey));
  }

  protected JSONArray getDataKeyedArrayJSON(JSONObject jsonObject, String objectKey)
    throws APIProtocolException
  {
    return (findJSONArrayInArray(getDataArrayJSON(jsonObject), objectKey));
  }

  private JSONObject findJSONObjectInArray(JSONArray jsonArray, String key)
  {
    JSONObject jsonObject = null;
    for (int index = 0; index < jsonArray.size() && jsonObject == null; index++)
      {
        try
          {
            jsonObject = (JSONObject) ((JSONObject) jsonArray.get(index)).get(key);
          }
        catch (Exception exception)
          {
            jsonObject = null;
          }
      }
    return (jsonObject);
  }

  private JSONArray findJSONArrayInArray(JSONArray jsonArray, String key)
  {
    JSONArray jsonFoundArray = null;
    for (int index = 0; index < jsonArray.size() && jsonFoundArray == null; index++)
      {
        try
          {
            jsonFoundArray = (JSONArray) ((JSONObject) jsonArray.get(index)).get(key);
          }
        catch (Exception exception)
          {
            jsonFoundArray = null;
          }
      }
    return (jsonFoundArray);
  }

  protected JSONArray getActionArrayJSON(JSONObject jsonObject)
    throws APIProtocolException
  {
    try
      {
        return ((JSONArray) getResponseJSON(jsonObject).get("action"));
      }
    catch (Exception exception)
      {
        throw new APIProtocolException(exception);
      }
  }

  protected JSONObject getActionKeyedObjectJSON(JSONObject jsonObject, String objectKey)
    throws APIProtocolException
  {
    return (findJSONObjectInArray(getActionArrayJSON(jsonObject), objectKey));
  }

  protected StatusCode getStatusCode(JSONObject jsonObject, APIVerbs apiVerb)
    throws APIProtocolException
  {
    try
      {
        StatusCode statusCode = StatusCode.valueOf((java.lang.Long) getInformationJSON(jsonObject).get("code"));
        if (apiVerb != null)
          if (statusCode == StatusCode.SUCCESSFUL)
            Session.getInstance().addPartialAPICall(apiVerb);
          else if (statusCode == StatusCode.PARTIAL_CONTENT)
            Session.getInstance().removePartialAPICall(apiVerb);
        return (statusCode);
      }
    catch (Exception exception)
      {
        throw new APIProtocolException(exception);
      }
  }

  protected StatusCode getStatusCode(JSONObject jsonObject)
    throws APIProtocolException
  {
    return (getStatusCode(jsonObject, null));
  }

  protected String getStatusMessage(JSONObject jsonObject)
    throws APIProtocolException
  {
    try
      {
        return ((String) getInformationJSON(jsonObject).get("status"));
      }
    catch (Exception exception)
      {
        throw new APIProtocolException(exception);
      }
  }

  public JSONObject doGetJSON(String url)
    throws APICommunicationException, APIProtocolException
  {
    Session.getInstance().addActiveRequestCount();
    long t1 = System.currentTimeMillis();
    JSONObject jsonObject = null;
    JSONParser jsonParser = new JSONParser();
    try
      {
        DefaultHttpClient httpclient = getHTTPClient();
        HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
        if (httpResponse.getStatusLine().getStatusCode() != 200)
          throw new APICommunicationException("HTTP Error " + httpResponse.getStatusLine().getStatusCode() + " - " + httpResponse.getStatusLine().getReasonPhrase() + " at " + url);
        HttpEntity httpEntity = httpResponse.getEntity();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpEntity.getContent()));
        jsonObject = (JSONObject) jsonParser.parse(bufferedReader);
        bufferedReader.close();
        httpclient.getConnectionManager().shutdown();
      }
    catch (ParseException parseException)
      {
        throw new APIProtocolException(parseException);
      }
    catch (IOException ioException)
      {
        throw new APICommunicationException(ioException);
      }
    catch (ClassCastException classCastException)
      {
        throw new APIProtocolException(classCastException);
      }
    catch (IllegalArgumentException illegalArgumentException)
      {
        throw new APICommunicationException(illegalArgumentException);
      }
    finally
      {
        Session.getInstance().removeActiveRequestCount();
        long t2 = System.currentTimeMillis();
        Debug.print(url + " : " + t1 + "-" + t2 + "=" + (t2 - t1) + " : " + jsonObject);
      }
    return (jsonObject);
  }

  private DefaultHttpClient getHTTPClient()
  {
    DefaultHttpClient httpclient = new DefaultHttpClient();
    httpclient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", _socketFactory, 443));
    httpclient.addRequestInterceptor(new HttpRequestInterceptor()
    {

      public void process(final HttpRequest request, final HttpContext context)
        throws HttpException, IOException
      {
        if (!request.containsHeader("Accept-Encoding"))
          {
            request.addHeader("Accept-Encoding", "gzip");
          }
      }
    });
    httpclient.addResponseInterceptor(new HttpResponseInterceptor()
    {

      public void process(final HttpResponse response, final HttpContext context)
        throws HttpException, IOException
      {
        HttpEntity entity = response.getEntity();
        Header ceheader = entity.getContentEncoding();
        if (ceheader != null)
          {
            HeaderElement[] codecs = ceheader.getElements();
            for (int index = 0; index < codecs.length; index++)
              if (codecs[index].getName().equalsIgnoreCase("gzip"))
                {
                  response.setEntity(new GzipDecompressingEntity(response.getEntity()));
                  return;
                }
          }
      }
    });
    return (httpclient);
  }

  private class GzipDecompressingEntity
    extends HttpEntityWrapper
  {

    public GzipDecompressingEntity(final HttpEntity entity)
    {
      super(entity);
    }

    @Override
    public InputStream getContent()
      throws IOException, IllegalStateException
    {
      InputStream wrappedin = wrappedEntity.getContent();
      return new GZIPInputStream(wrappedin);
    }

    @Override
    public long getContentLength()
    {
      return -1;
    }
  }
}
