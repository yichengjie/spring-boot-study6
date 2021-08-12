1. 腾讯接口文档：https://developers.weixin.qq.com/miniprogram/dev/framework/open-ability/qr-code.html

### 页面参数较长，生成个数量限制

1. 可接受 path 参数较长，生成个数受限，数量限制见 ，请谨慎使用

2. API地址：https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/qr-code/wxacode.get.html

3. 接口地址：POST https://api.weixin.qq.com/wxa/getwxacode?access_token=ACCESS_TOKEN

4. 重点参数

   | 属性 | 类型   | 默认值 | 必填 | 说明                                                         |
   | ---- | ------ | ------ | ---- | ------------------------------------------------------------ |
   | path | string | 空     | 是   | 扫码进入的小程序页面路径，最大长度 128 字节，不能为空；对于小游戏，<br />可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"，<br />即可在 `wx.getLaunchOptionsSync` 接口中的 query 参数获取到 `{foo:"bar"}`。 |

       4. 注意事项
          - POST 参数需要转成 JSON 字符串，不支持 form 表单提交。
          - 接口只能生成已发布的小程序的二维码
          - 与 [wxacode.createQRCode](https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/qr-code/wxacode.createQRCode.html) 总共生成的码数量限制为 100,000，请谨慎调用。

### 页面参数较短，生成个数不受限

1. API地址：https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/qr-code/wxacode.getUnlimited.html

2. 接口地址：POST https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=ACCESS_TOKEN

3. 重点参数

   | 属性  | 类型   | 默认值 | 必填 | 说明                                                         |
   | ----- | ------ | ------ | ---- | ------------------------------------------------------------ |
   | scene | string | 空     | 是   | 最大32个可见字符，只支持数字，大小写英文以及部分特殊字符：!#$&'()*+,/:;=?@-._~，<br />其它字符请自行编码为合法字符（因不支持%，中文无法使用 urlencode 处理，请使用其他编码方式） |
   | page  | string | 主页   | 否   | 必须是已经发布的小程序存在的页面（否则报错），例如 `pages/index/index`, 根路径前<br />不要填加 `/`,不能携带参数（参数请放在scene字段里），如果不填写这个字段，默认跳主页面 |

4. 注意事项：

   - POST 参数需要转成 JSON 字符串，不支持 form 表单提交。
   - 接口只能生成已发布的小程序的二维码
   - 调用分钟频率受限（5000次/分钟），如需大量小程序码，建议预生成

### Java生成示例

1. 二维码生成工具类

   ```java
   @Component
   @Slf4j
   public class WxQrCode {
       //获取AccessToken路径
       private static final String AccessToken_URL
               = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";//小程序id
       /**
        * 用于获取access_token
        * @return access_token
        * @throws Exception
        */
       public static String getAccessToken(String appid, String secret) throws Exception {
           String requestUrl = AccessToken_URL.replace("APPID", appid).replace("APPSECRET", secret);
           URL url = new URL(requestUrl);
           // 打开和URL之间的连接
           HttpURLConnection connection = (HttpURLConnection) url.openConnection();
           connection.setRequestMethod("POST");
           // 设置通用的请求属性
           connection.setRequestProperty("Content-Type", "application/json");
           connection.setRequestProperty("Connection", "Keep-Alive");
           connection.setUseCaches(false);
           connection.setDoOutput(true);
           connection.setDoInput(true);
           // 得到请求的输出流对象
           DataOutputStream out = new DataOutputStream(connection.getOutputStream());
           out.writeBytes("");
           out.flush();
           out.close();
           // 建立实际的连接
           connection.connect();
           // 定义 BufferedReader输入流来读取URL的响应
           BufferedReader in = null;
           if (requestUrl.contains("nlp"))
               in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "GBK"));
           else
               in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
           String result = "";
           String getLine;
           while ((getLine = in.readLine()) != null) {
               result += getLine;
           }
           in.close();
           JSONObject jsonObject = JSON.parseObject(result);
           String accesstoken = jsonObject.getString("access_token");
           return accesstoken;
       }
   
       /**
        * 生成不限个数的小程序码（建议使用）
        * @param accessToken
        * @param filePath
        * @return
        */
       public static void getminiqrQr(String accessToken, String filePath) {
           String WxCode_URL = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=ACCESS_TOKEN";//小程序密钥
           String fileName = "code1.png";
           try {
               String wxCodeURL = WxCode_URL.replace("ACCESS_TOKEN", accessToken);
               URL url = new URL(wxCodeURL);
               HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
               httpURLConnection.setRequestMethod("POST");// 提交模式
               // conn.setConnectTimeout(10000);//连接超时 单位毫秒
               // conn.setReadTimeout(2000);//读取超时 单位毫秒
               // 发送POST请求必须设置如下两行
               httpURLConnection.setDoOutput(true);
               httpURLConnection.setDoInput(true);
               // 获取URLConnection对象对应的输出流
               PrintWriter printWriter = new PrintWriter(httpURLConnection.getOutputStream());
               // 发送请求参数
               JSONObject paramJson = new JSONObject();
               // 注意小程序页面如果不存在，则注释掉page参数
               //paramJson.put("page", "pages/index/index");
               paramJson.put("scene", "1234567890");
               paramJson.put("width", 430);
               paramJson.put("is_hyaline", true);
               paramJson.put("auto_color", true);
               printWriter.write(paramJson.toString());
               // flush输出流的缓冲
               printWriter.flush();
               //开始获取数据
               BufferedInputStream bis = new BufferedInputStream(httpURLConnection.getInputStream());
               OutputStream os = new FileOutputStream(new File(filePath + fileName));
               int len;
               byte[] arr = new byte[1024];
               while ((len = bis.read(arr)) != -1) {
                   os.write(arr, 0, len);
                   os.flush();
               }
               os.close();
           } catch (Exception e) {
               log.error("===> error : ", e);
           }
       }
   
   
       /**
        * 生成限制个数的小程序码（不建议使用）
        * @param accessToken
        * @param filePath
        * @return
        */
       public static void getminiqrQr2(String accessToken, String filePath) {
           // bad
           String WxCode_URL = "https://api.weixin.qq.com/wxa/getwxacode?access_token=ACCESS_TOKEN" ;
           String fileName = "code2.png";
           try {
               String wxCodeURL = WxCode_URL.replace("ACCESS_TOKEN", accessToken);
               URL url = new URL(wxCodeURL);
               HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
               httpURLConnection.setRequestMethod("POST");// 提交模式
               // conn.setConnectTimeout(10000);//连接超时 单位毫秒
               // conn.setReadTimeout(2000);//读取超时 单位毫秒
               // 发送POST请求必须设置如下两行
               httpURLConnection.setDoOutput(true);
               httpURLConnection.setDoInput(true);
               // 获取URLConnection对象对应的输出流
               PrintWriter printWriter = new PrintWriter(httpURLConnection.getOutputStream());
               // 发送请求参数
               JSONObject paramJson = new JSONObject();
               paramJson.put("path", "pages/index/index");
               paramJson.put("width", 430);
               paramJson.put("is_hyaline", true);
               paramJson.put("auto_color", true);
               printWriter.write(paramJson.toString());
               // flush输出流的缓冲
               printWriter.flush();
               //开始获取数据
               BufferedInputStream bis = new BufferedInputStream(httpURLConnection.getInputStream());
               OutputStream os = new FileOutputStream(new File(filePath + fileName));
               int len;
               byte[] arr = new byte[1024];
               while ((len = bis.read(arr)) != -1) {
                   os.write(arr, 0, len);
                   os.flush();
               }
               os.close();
           } catch (Exception e) {
               log.error("===> error : ", e);
           }
       }
   }
   ```

2. Controller 测试类

   ```java
   @RestController
   public class HelloController {
       @Value("${wx.appid}")
       private String appIdY;//小程序id
       @Value("${wx.appsecret}")
       private String appSecret ;//小程序密钥
       private String filePath = "D:\\code\\";
   
       @GetMapping(value="/code")
       public String twoCode(HttpServletRequest request) throws Exception {
           String accessToken = WxQrCode.getAccessToken(appIdY,appSecret);
           WxQrCode.getminiqrQr(accessToken,filePath);
           return  "hello world" ;
       }
   
       @GetMapping(value="/code2")
       public String twoCode2(HttpServletRequest request) throws Exception {
           String accessToken = WxQrCode.getAccessToken(appIdY,appSecret);
           WxQrCode.getminiqrQr2(accessToken,filePath) ;
           return "hello world";
       }
   }
   ```






