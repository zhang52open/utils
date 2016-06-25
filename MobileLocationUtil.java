package com.froad.test.order;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
 
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
 
public class MobileLocationUtil {
    /**
     * 归属地查询
     * @param mobile
     * @return mobileAddress
     */
    @SuppressWarnings("unused")
    private static String getLocationByMobile(final String mobile) throws ParserConfigurationException, SAXException, IOException{ 
        String MOBILEURL = " http://www.youdao.com/smartresult-xml/search.s?type=mobile&q="; 
        String result = callUrlByGet(MOBILEURL + mobile, "GBK");
        StringReader stringReader = new StringReader(result); 
        InputSource inputSource = new InputSource(stringReader); 
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance(); 
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder(); 
        Document document = documentBuilder.parse(inputSource);
 
        if (!(document.getElementsByTagName("location").item(0) == null)) {
            return document.getElementsByTagName("location").item(0).getFirstChild().getNodeValue();
        }else{
            return "无此号记录！";
        }
    }
    /**
     * 获取URL返回的字符串
     * @param callurl
     * @param charset
     * @return
     */
    private static String callUrlByGet(String callurl,String charset){   
        String result = "";   
        try {   
            URL url = new URL(callurl);   
            URLConnection connection = url.openConnection();   
            connection.connect();   
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),charset));   
            String line;   
            while((line = reader.readLine())!= null){    
                result += line;   
                result += "\n";
            }
        } catch (Exception e) {   
            e.printStackTrace();   
            return "";
        }
        return result;
    }
    /**
     * 手机号码归属地
     * @param tel  手机号码
     * @return 135XXXXXXXX,联通/移动/电信,湖北武汉
     * @throws Exception
     * @author 
     */
    public static String getMobileLocation(String tel) throws Exception{
        Pattern pattern = Pattern.compile("1\\d{10}");
        Matcher matcher = pattern.matcher(tel);
        if(matcher.matches()){
            String url = "http://life.tenpay.com/cgi-bin/mobile/MobileQueryAttribution.cgi?chgmobile=" + tel;
            String result = callUrlByGet(url,"GBK");
            StringReader stringReader = new StringReader(result); 
            InputSource inputSource = new InputSource(stringReader); 
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance(); 
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder(); 
            Document document = documentBuilder.parse(inputSource);
            String retmsg = document.getElementsByTagName("retmsg").item(0).getFirstChild().getNodeValue();
            if(retmsg.equals("OK")){
                String supplier = document.getElementsByTagName("supplier").item(0).getFirstChild().getNodeValue().trim();
                String province = document.getElementsByTagName("province").item(0).getFirstChild().getNodeValue().trim();
                String city = document.getElementsByTagName("city").item(0).getFirstChild().getNodeValue().trim();
                if (province.equals("-") || city.equals("-")) {

//                    return (tel + "," + supplier + ","+ getLocationByMobile(tel));
                    return (getLocationByMobile(tel) + "," + supplier);
                }else {

//                    return (tel + "," + supplier + ","+ province + city);
                    return (province + city + "," + supplier );
                }

            }else {

                return "无此号记录！";

            }

        }else{

            return tel+ "：手机号码格式错误！";

        }

    }
    
    
    public static void main(String[] args) {
        try {
            System.out.println(MobileLocationUtil.getMobileLocation("187****254"));
            System.out.println(MobileLocationUtil.getMobileLocation("18684306765"));
            System.out.println(MobileLocationUtil.getMobileLocation("15944339065"));
            System.out.println(MobileLocationUtil.getMobileLocation("13562687889"));
            System.out.println(MobileLocationUtil.getMobileLocation("15288810176"));
            System.out.println(MobileLocationUtil.getMobileLocation("13465151789"));
            System.out.println(MobileLocationUtil.getMobileLocation("13954109680"));
            System.out.println(MobileLocationUtil.getMobileLocation("15504421672"));
            System.out.println(MobileLocationUtil.getMobileLocation("13953686525"));
            System.out.println(MobileLocationUtil.getMobileLocation("15568879577"));
            System.out.println(MobileLocationUtil.getMobileLocation("15981192023"));
            System.out.println(MobileLocationUtil.getMobileLocation("13944086160"));
            System.out.println(MobileLocationUtil.getMobileLocation("18686407373"));
            System.out.println(MobileLocationUtil.getMobileLocation("15981249173"));
            System.out.println(MobileLocationUtil.getMobileLocation("13964785128"));
            System.out.println(MobileLocationUtil.getMobileLocation("15095105896"));
            System.out.println(MobileLocationUtil.getMobileLocation("18660663738"));
            System.out.println(MobileLocationUtil.getMobileLocation("15153648930"));
            System.out.println(MobileLocationUtil.getMobileLocation("15095133623"));
            System.out.println(MobileLocationUtil.getMobileLocation("13280714778"));
            System.out.println(MobileLocationUtil.getMobileLocation("18863619896"));
            System.out.println(MobileLocationUtil.getMobileLocation("15964569986"));
            System.out.println(MobileLocationUtil.getMobileLocation("15169592027"));
            System.out.println(MobileLocationUtil.getMobileLocation("13843896167"));
            System.out.println(MobileLocationUtil.getMobileLocation("15066707808"));
            System.out.println(MobileLocationUtil.getMobileLocation("13179254423"));
            System.out.println(MobileLocationUtil.getMobileLocation("15688904110"));
            System.out.println(MobileLocationUtil.getMobileLocation("13804360048"));
            System.out.println(MobileLocationUtil.getMobileLocation("18629905551"));
            System.out.println(MobileLocationUtil.getMobileLocation("15944068789"));
            System.out.println(MobileLocationUtil.getMobileLocation("15043303211"));
            System.out.println(MobileLocationUtil.getMobileLocation("13843303053"));
            System.out.println(MobileLocationUtil.getMobileLocation("13964630626"));
            System.out.println(MobileLocationUtil.getMobileLocation("18615362228"));
            System.out.println(MobileLocationUtil.getMobileLocation("15664491803"));
            System.out.println(MobileLocationUtil.getMobileLocation("18375618540"));
            System.out.println(MobileLocationUtil.getMobileLocation("15624120888"));
            System.out.println(MobileLocationUtil.getMobileLocation("15906363055"));
            System.out.println(MobileLocationUtil.getMobileLocation("13792682152"));
            System.out.println(MobileLocationUtil.getMobileLocation("15964901387"));
            System.out.println(MobileLocationUtil.getMobileLocation("15653616351"));
            System.out.println(MobileLocationUtil.getMobileLocation("18653608209"));
            System.out.println(MobileLocationUtil.getMobileLocation("15966110673"));
            System.out.println(MobileLocationUtil.getMobileLocation("13953619214"));
            System.out.println(MobileLocationUtil.getMobileLocation("13583613111"));
            System.out.println(MobileLocationUtil.getMobileLocation("17078604303"));
            System.out.println(MobileLocationUtil.getMobileLocation("18143033477"));
            System.out.println(MobileLocationUtil.getMobileLocation("13869671550"));
            System.out.println(MobileLocationUtil.getMobileLocation("13843312381"));
            System.out.println(MobileLocationUtil.getMobileLocation("15910050667"));
            System.out.println(MobileLocationUtil.getMobileLocation("13165269777"));
            System.out.println(MobileLocationUtil.getMobileLocation("18353661597"));
            System.out.println(MobileLocationUtil.getMobileLocation("15866534815"));
            System.out.println(MobileLocationUtil.getMobileLocation("15944734670"));
            System.out.println(MobileLocationUtil.getMobileLocation("18653622843"));
            System.out.println(MobileLocationUtil.getMobileLocation("18243896817"));
            System.out.println(MobileLocationUtil.getMobileLocation("13371078267"));
            System.out.println(MobileLocationUtil.getMobileLocation("13853679825"));
            System.out.println(MobileLocationUtil.getMobileLocation("13944720726"));
            System.out.println(MobileLocationUtil.getMobileLocation("13854447076"));
            System.out.println(MobileLocationUtil.getMobileLocation("15662474235"));
            System.out.println(MobileLocationUtil.getMobileLocation("13963655875"));
            System.out.println(MobileLocationUtil.getMobileLocation("15964902027"));
            System.out.println(MobileLocationUtil.getMobileLocation("15949757464"));
            System.out.println(MobileLocationUtil.getMobileLocation("15944339696"));
            System.out.println(MobileLocationUtil.getMobileLocation("15763011750"));
            System.out.println(MobileLocationUtil.getMobileLocation("15853606376"));
            System.out.println(MobileLocationUtil.getMobileLocation("13766058911"));
            System.out.println(MobileLocationUtil.getMobileLocation("18725791484"));
            System.out.println(MobileLocationUtil.getMobileLocation("13756507371"));
            System.out.println(MobileLocationUtil.getMobileLocation("13596943210"));
            System.out.println(MobileLocationUtil.getMobileLocation("15843847228"));
            System.out.println(MobileLocationUtil.getMobileLocation("18643508558"));
            System.out.println(MobileLocationUtil.getMobileLocation("13894150091"));
            System.out.println(MobileLocationUtil.getMobileLocation("13756079129"));
            System.out.println(MobileLocationUtil.getMobileLocation("14743991174"));
            System.out.println(MobileLocationUtil.getMobileLocation("13894726975"));
            System.out.println(MobileLocationUtil.getMobileLocation("15124402400"));
            System.out.println(MobileLocationUtil.getMobileLocation("15866167773"));
            System.out.println(MobileLocationUtil.getMobileLocation("13009095999"));
            System.out.println(MobileLocationUtil.getMobileLocation("18843235207"));
            System.out.println(MobileLocationUtil.getMobileLocation("15966133328"));
            System.out.println(MobileLocationUtil.getMobileLocation("13943826686"));
            System.out.println(MobileLocationUtil.getMobileLocation("13943097798"));
            System.out.println(MobileLocationUtil.getMobileLocation("13354301116"));
            System.out.println(MobileLocationUtil.getMobileLocation("13350394909"));
            System.out.println(MobileLocationUtil.getMobileLocation("13322457898"));
            System.out.println(MobileLocationUtil.getMobileLocation("13756789527"));
            System.out.println(MobileLocationUtil.getMobileLocation("15543738998"));
            System.out.println(MobileLocationUtil.getMobileLocation("13500886757"));
            System.out.println(MobileLocationUtil.getMobileLocation("13798896714"));
            System.out.println(MobileLocationUtil.getMobileLocation("13224459359"));
            System.out.println(MobileLocationUtil.getMobileLocation("15006603089"));
            System.out.println(MobileLocationUtil.getMobileLocation("18043084601"));
            System.out.println(MobileLocationUtil.getMobileLocation("13721484891"));
            System.out.println(MobileLocationUtil.getMobileLocation("18663684615"));
            System.out.println(MobileLocationUtil.getMobileLocation("15243212462"));
            System.out.println(MobileLocationUtil.getMobileLocation("13515361709"));
            System.out.println(MobileLocationUtil.getMobileLocation("15948639846"));
            System.out.println(MobileLocationUtil.getMobileLocation("13508965097"));
            System.out.println(MobileLocationUtil.getMobileLocation("15124574511"));
            System.out.println(MobileLocationUtil.getMobileLocation("15804493916"));
            System.out.println(MobileLocationUtil.getMobileLocation("18844270700"));
            System.out.println(MobileLocationUtil.getMobileLocation("15590065156"));
            System.out.println(MobileLocationUtil.getMobileLocation("13626369280"));
            System.out.println(MobileLocationUtil.getMobileLocation("13404666961"));
            System.out.println(MobileLocationUtil.getMobileLocation("15842624628"));
            System.out.println(MobileLocationUtil.getMobileLocation("13009136602"));
            System.out.println(MobileLocationUtil.getMobileLocation("15585555686"));
            System.out.println(MobileLocationUtil.getMobileLocation("13069176145"));
            System.out.println(MobileLocationUtil.getMobileLocation("13644465700"));
            System.out.println(MobileLocationUtil.getMobileLocation("13804418812"));
            System.out.println(MobileLocationUtil.getMobileLocation("13258847629"));
            System.out.println(MobileLocationUtil.getMobileLocation("13674419006"));
            System.out.println(MobileLocationUtil.getMobileLocation("13404595296"));
            System.out.println(MobileLocationUtil.getMobileLocation("13620744123"));
            System.out.println(MobileLocationUtil.getMobileLocation("18744517454"));
            System.out.println(MobileLocationUtil.getMobileLocation("18704491923"));
            System.out.println(MobileLocationUtil.getMobileLocation("13869618575"));
            System.out.println(MobileLocationUtil.getMobileLocation("15143212159"));
            System.out.println(MobileLocationUtil.getMobileLocation("18654727272"));
            System.out.println(MobileLocationUtil.getMobileLocation("13573681185"));
            System.out.println(MobileLocationUtil.getMobileLocation("15590664091"));
            System.out.println(MobileLocationUtil.getMobileLocation("15943807418"));
            System.out.println(MobileLocationUtil.getMobileLocation("15145124421"));
            System.out.println(MobileLocationUtil.getMobileLocation("15143558967"));
            System.out.println(MobileLocationUtil.getMobileLocation("15584008999"));
            System.out.println(MobileLocationUtil.getMobileLocation("18744588848"));
            System.out.println(MobileLocationUtil.getMobileLocation("18244009690"));
            System.out.println(MobileLocationUtil.getMobileLocation("13173120500"));
            System.out.println(MobileLocationUtil.getMobileLocation("13176469937"));
            System.out.println(MobileLocationUtil.getMobileLocation("15948917218"));
            System.out.println(MobileLocationUtil.getMobileLocation("15948825773"));
            System.out.println(MobileLocationUtil.getMobileLocation("15584100765"));
            System.out.println(MobileLocationUtil.getMobileLocation("13853635323"));
            System.out.println(MobileLocationUtil.getMobileLocation("15043440436"));
            System.out.println(MobileLocationUtil.getMobileLocation("15590539551"));
            System.out.println(MobileLocationUtil.getMobileLocation("15253690720"));
            System.out.println(MobileLocationUtil.getMobileLocation("13756519021"));
            System.out.println(MobileLocationUtil.getMobileLocation("15949757398"));
            System.out.println(MobileLocationUtil.getMobileLocation("13562681118"));
            System.out.println(MobileLocationUtil.getMobileLocation("15168807805"));
            System.out.println(MobileLocationUtil.getMobileLocation("15843776925"));
            System.out.println(MobileLocationUtil.getMobileLocation("13178303444"));
            System.out.println(MobileLocationUtil.getMobileLocation("18943205230"));
            System.out.println(MobileLocationUtil.getMobileLocation("13953636701"));
            System.out.println(MobileLocationUtil.getMobileLocation("17863349367"));
            System.out.println(MobileLocationUtil.getMobileLocation("13780828155"));
            System.out.println(MobileLocationUtil.getMobileLocation("15124374109"));
            System.out.println(MobileLocationUtil.getMobileLocation("18628766936"));
            System.out.println(MobileLocationUtil.getMobileLocation("18943022687"));
            System.out.println(MobileLocationUtil.getMobileLocation("18686539727"));
            System.out.println(MobileLocationUtil.getMobileLocation("13791606806"));
            System.out.println(MobileLocationUtil.getMobileLocation("15621685801"));
            System.out.println(MobileLocationUtil.getMobileLocation("15164303424"));
            System.out.println(MobileLocationUtil.getMobileLocation("13331753131"));
            System.out.println(MobileLocationUtil.getMobileLocation("15576023998"));
            System.out.println(MobileLocationUtil.getMobileLocation("15044333200"));
            System.out.println(MobileLocationUtil.getMobileLocation("15662548648"));
            System.out.println(MobileLocationUtil.getMobileLocation("13963612965"));
            System.out.println(MobileLocationUtil.getMobileLocation("15043883400"));
            System.out.println(MobileLocationUtil.getMobileLocation("13070725376"));
            System.out.println(MobileLocationUtil.getMobileLocation("13011696159"));
            System.out.println(MobileLocationUtil.getMobileLocation("13589158910"));
            System.out.println(MobileLocationUtil.getMobileLocation("18162088047"));
            System.out.println(MobileLocationUtil.getMobileLocation("13792685441"));
            System.out.println(MobileLocationUtil.getMobileLocation("15288806273"));
            System.out.println(MobileLocationUtil.getMobileLocation("15981330075"));
            System.out.println(MobileLocationUtil.getMobileLocation("13214499516"));
            System.out.println(MobileLocationUtil.getMobileLocation("18706577998"));
            System.out.println(MobileLocationUtil.getMobileLocation("15590111117"));
            System.out.println(MobileLocationUtil.getMobileLocation("13220738806"));
            System.out.println(MobileLocationUtil.getMobileLocation("13689775767"));
            System.out.println(MobileLocationUtil.getMobileLocation("13843580623"));
            System.out.println(MobileLocationUtil.getMobileLocation("13604432330"));
            System.out.println(MobileLocationUtil.getMobileLocation("18843450667"));
            System.out.println(MobileLocationUtil.getMobileLocation("13166972895"));
            System.out.println(MobileLocationUtil.getMobileLocation("13844654559"));
            System.out.println(MobileLocationUtil.getMobileLocation("13604469872"));
            System.out.println(MobileLocationUtil.getMobileLocation("13196203111"));
            System.out.println(MobileLocationUtil.getMobileLocation("15568448656"));
            System.out.println(MobileLocationUtil.getMobileLocation("13278240323"));
            System.out.println(MobileLocationUtil.getMobileLocation("18444111996"));
            System.out.println(MobileLocationUtil.getMobileLocation("13280182974"));
            System.out.println(MobileLocationUtil.getMobileLocation("13044314126"));
            System.out.println(MobileLocationUtil.getMobileLocation("15823519989"));
            System.out.println(MobileLocationUtil.getMobileLocation("15843486126"));
            System.out.println(MobileLocationUtil.getMobileLocation("13258075883"));
            System.out.println(MobileLocationUtil.getMobileLocation("13596634469"));
            System.out.println(MobileLocationUtil.getMobileLocation("15823160019"));
            System.out.println(MobileLocationUtil.getMobileLocation("18604337329"));
            System.out.println(MobileLocationUtil.getMobileLocation("15662276771"));
            System.out.println(MobileLocationUtil.getMobileLocation("18443117020"));
            System.out.println(MobileLocationUtil.getMobileLocation("15568873432"));
            System.out.println(MobileLocationUtil.getMobileLocation("13943895413"));
            System.out.println(MobileLocationUtil.getMobileLocation("18845011341"));
            System.out.println(MobileLocationUtil.getMobileLocation("15568861122"));
            System.out.println(MobileLocationUtil.getMobileLocation("15963622177"));
            System.out.println(MobileLocationUtil.getMobileLocation("13404791515"));
            System.out.println(MobileLocationUtil.getMobileLocation("15615362873"));
            System.out.println(MobileLocationUtil.getMobileLocation("15165632328"));
            System.out.println(MobileLocationUtil.getMobileLocation("13944320680"));
            System.out.println(MobileLocationUtil.getMobileLocation("15006626381"));
            System.out.println(MobileLocationUtil.getMobileLocation("15169607298"));
            System.out.println(MobileLocationUtil.getMobileLocation("15567756664"));
            System.out.println(MobileLocationUtil.getMobileLocation("18744410818"));
            System.out.println(MobileLocationUtil.getMobileLocation("15143448623"));
            System.out.println(MobileLocationUtil.getMobileLocation("13106052648"));
            System.out.println(MobileLocationUtil.getMobileLocation("13596862710"));
            System.out.println(MobileLocationUtil.getMobileLocation("13214471516"));
            System.out.println(MobileLocationUtil.getMobileLocation("13610773572"));
            System.out.println(MobileLocationUtil.getMobileLocation("18743449193"));
            System.out.println(MobileLocationUtil.getMobileLocation("13127175359"));
            System.out.println(MobileLocationUtil.getMobileLocation("15265446712"));
            System.out.println(MobileLocationUtil.getMobileLocation("15124449605"));
            System.out.println(MobileLocationUtil.getMobileLocation("15953620634"));
            System.out.println(MobileLocationUtil.getMobileLocation("13181658968"));
            System.out.println(MobileLocationUtil.getMobileLocation("13894701070"));
            System.out.println(MobileLocationUtil.getMobileLocation("13964657180"));
            System.out.println(MobileLocationUtil.getMobileLocation("15843448722"));
            System.out.println(MobileLocationUtil.getMobileLocation("13578639539"));
            System.out.println(MobileLocationUtil.getMobileLocation("15043025506"));
            System.out.println(MobileLocationUtil.getMobileLocation("13843393668"));
            System.out.println(MobileLocationUtil.getMobileLocation("18660612658"));
            System.out.println(MobileLocationUtil.getMobileLocation("13843644688"));
            System.out.println(MobileLocationUtil.getMobileLocation("13721949732"));
            System.out.println(MobileLocationUtil.getMobileLocation("13894203150"));
            System.out.println(MobileLocationUtil.getMobileLocation("18363656616"));
            System.out.println(MobileLocationUtil.getMobileLocation("13176728167"));
            System.out.println(MobileLocationUtil.getMobileLocation("15904312215"));
            System.out.println(MobileLocationUtil.getMobileLocation("15166366600"));
            System.out.println(MobileLocationUtil.getMobileLocation("13070757877"));
            System.out.println(MobileLocationUtil.getMobileLocation("13606363187"));
            System.out.println(MobileLocationUtil.getMobileLocation("13371071829"));
            System.out.println(MobileLocationUtil.getMobileLocation("15854405015"));
            System.out.println(MobileLocationUtil.getMobileLocation("18660651086"));
            System.out.println(MobileLocationUtil.getMobileLocation("13943828562"));
            System.out.println(MobileLocationUtil.getMobileLocation("15585528680"));
            System.out.println(MobileLocationUtil.getMobileLocation("13863684958"));
            System.out.println(MobileLocationUtil.getMobileLocation("15163679607"));
            System.out.println(MobileLocationUtil.getMobileLocation("15806477526"));
            System.out.println(MobileLocationUtil.getMobileLocation("13589160369"));
            System.out.println(MobileLocationUtil.getMobileLocation("13465666300"));
            System.out.println(MobileLocationUtil.getMobileLocation("13864651314"));
            System.out.println(MobileLocationUtil.getMobileLocation("13515401336"));
            System.out.println(MobileLocationUtil.getMobileLocation("13863646375"));
            System.out.println(MobileLocationUtil.getMobileLocation("13364465575"));
            System.out.println(MobileLocationUtil.getMobileLocation("15948808068"));
            System.out.println(MobileLocationUtil.getMobileLocation("15144488321"));
            System.out.println(MobileLocationUtil.getMobileLocation("15134428799"));
            System.out.println(MobileLocationUtil.getMobileLocation("13081697775"));
            System.out.println(MobileLocationUtil.getMobileLocation("18943527960"));
            System.out.println(MobileLocationUtil.getMobileLocation("15662822988"));
            System.out.println(MobileLocationUtil.getMobileLocation("13944789102"));
            System.out.println(MobileLocationUtil.getMobileLocation("15144200008"));
            System.out.println(MobileLocationUtil.getMobileLocation("17704301357"));
            System.out.println(MobileLocationUtil.getMobileLocation("18543478560"));
            System.out.println(MobileLocationUtil.getMobileLocation("13869671781"));
            System.out.println(MobileLocationUtil.getMobileLocation("13205366669"));
            System.out.println(MobileLocationUtil.getMobileLocation("18663608077"));
            System.out.println(MobileLocationUtil.getMobileLocation("15253634236"));
            System.out.println(MobileLocationUtil.getMobileLocation("15590628127"));
            System.out.println(MobileLocationUtil.getMobileLocation("13981368886"));
            System.out.println(MobileLocationUtil.getMobileLocation("15526529087"));
            System.out.println(MobileLocationUtil.getMobileLocation("15844957345"));
            System.out.println(MobileLocationUtil.getMobileLocation("18543478557"));
            System.out.println(MobileLocationUtil.getMobileLocation("13943636934"));
            System.out.println(MobileLocationUtil.getMobileLocation("18571450986"));
            System.out.println(MobileLocationUtil.getMobileLocation("18863610730"));
            System.out.println(MobileLocationUtil.getMobileLocation("13589565695"));
            System.out.println(MobileLocationUtil.getMobileLocation("13944477697"));
            System.out.println(MobileLocationUtil.getMobileLocation("18643481799"));
            System.out.println(MobileLocationUtil.getMobileLocation("15044206652"));
            System.out.println(MobileLocationUtil.getMobileLocation("13583641366"));
            System.out.println(MobileLocationUtil.getMobileLocation("13853662202"));
            System.out.println(MobileLocationUtil.getMobileLocation("13756566190"));
            System.out.println(MobileLocationUtil.getMobileLocation("13756661000"));
            System.out.println(MobileLocationUtil.getMobileLocation("18629923352"));
            System.out.println(MobileLocationUtil.getMobileLocation("15288995557"));
            System.out.println(MobileLocationUtil.getMobileLocation("15621662195"));
            System.out.println(MobileLocationUtil.getMobileLocation("15585477758"));
            System.out.println(MobileLocationUtil.getMobileLocation("15953669346"));
            System.out.println(MobileLocationUtil.getMobileLocation("15953669246"));
            System.out.println(MobileLocationUtil.getMobileLocation("15653671917"));
            System.out.println(MobileLocationUtil.getMobileLocation("13630588517"));
            System.out.println(MobileLocationUtil.getMobileLocation("18723674295"));
            System.out.println(MobileLocationUtil.getMobileLocation("13805367063"));
            System.out.println(MobileLocationUtil.getMobileLocation("13404592179"));
            System.out.println(MobileLocationUtil.getMobileLocation("15064426269"));
            System.out.println(MobileLocationUtil.getMobileLocation("15863263469"));
            System.out.println(MobileLocationUtil.getMobileLocation("13625360326"));
            System.out.println(MobileLocationUtil.getMobileLocation("18506457200"));
            System.out.println(MobileLocationUtil.getMobileLocation("15948126656"));
            System.out.println(MobileLocationUtil.getMobileLocation("13364566996"));
            System.out.println(MobileLocationUtil.getMobileLocation("18663690519"));
            System.out.println(MobileLocationUtil.getMobileLocation("15169507519"));
            System.out.println(MobileLocationUtil.getMobileLocation("13756976300"));
            System.out.println(MobileLocationUtil.getMobileLocation("13563662299"));
            System.out.println(MobileLocationUtil.getMobileLocation("15966108489"));
            System.out.println(MobileLocationUtil.getMobileLocation("13573655010"));
            System.out.println(MobileLocationUtil.getMobileLocation("13214400711"));
            System.out.println(MobileLocationUtil.getMobileLocation("13863669392"));
            System.out.println(MobileLocationUtil.getMobileLocation("15053382265"));
            System.out.println(MobileLocationUtil.getMobileLocation("13053679333"));
            System.out.println(MobileLocationUtil.getMobileLocation("15665981369"));
            System.out.println(MobileLocationUtil.getMobileLocation("18946794756"));
            System.out.println(MobileLocationUtil.getMobileLocation("18889979895"));
            System.out.println(MobileLocationUtil.getMobileLocation("15043728611"));
            System.out.println(MobileLocationUtil.getMobileLocation("13070700603"));
            System.out.println(MobileLocationUtil.getMobileLocation("17056125678"));
            System.out.println(MobileLocationUtil.getMobileLocation("15904429760"));
            System.out.println(MobileLocationUtil.getMobileLocation("13563680799"));
            System.out.println(MobileLocationUtil.getMobileLocation("13620706963"));
            System.out.println(MobileLocationUtil.getMobileLocation("15844410573"));
            System.out.println(MobileLocationUtil.getMobileLocation("13792618940"));
            System.out.println(MobileLocationUtil.getMobileLocation("13630722899"));
            System.out.println(MobileLocationUtil.getMobileLocation("15095915333"));
            System.out.println(MobileLocationUtil.getMobileLocation("13843376400"));
            System.out.println(MobileLocationUtil.getMobileLocation("15043822211"));
            System.out.println(MobileLocationUtil.getMobileLocation("13214479427"));
            System.out.println(MobileLocationUtil.getMobileLocation("15948022467"));
            System.out.println(MobileLocationUtil.getMobileLocation("15568953885"));
            System.out.println(MobileLocationUtil.getMobileLocation("13500940024"));
            System.out.println(MobileLocationUtil.getMobileLocation("18643661929"));
            System.out.println(MobileLocationUtil.getMobileLocation("13578984697"));
            System.out.println(MobileLocationUtil.getMobileLocation("15898934667"));
            System.out.println(MobileLocationUtil.getMobileLocation("15354609596"));
            System.out.println(MobileLocationUtil.getMobileLocation("15942334676"));
            System.out.println(MobileLocationUtil.getMobileLocation("17783017279"));
            System.out.println(MobileLocationUtil.getMobileLocation("15754419009"));
            System.out.println(MobileLocationUtil.getMobileLocation("15165666956"));
            System.out.println(MobileLocationUtil.getMobileLocation("13844908858"));
            System.out.println(MobileLocationUtil.getMobileLocation("13578704408"));
            System.out.println(MobileLocationUtil.getMobileLocation("15243202521"));
            System.out.println(MobileLocationUtil.getMobileLocation("15243011146"));
            System.out.println(MobileLocationUtil.getMobileLocation("13596850101"));
            System.out.println(MobileLocationUtil.getMobileLocation("13180867771"));
            System.out.println(MobileLocationUtil.getMobileLocation("13843132612"));
            System.out.println(MobileLocationUtil.getMobileLocation("15590483266"));
            System.out.println(MobileLocationUtil.getMobileLocation("18629975403"));
            System.out.println(MobileLocationUtil.getMobileLocation("13101151801"));
            System.out.println(MobileLocationUtil.getMobileLocation("18581285566"));
            System.out.println(MobileLocationUtil.getMobileLocation("18744301159"));
            System.out.println(MobileLocationUtil.getMobileLocation("15144024201"));
            System.out.println(MobileLocationUtil.getMobileLocation("15104322161"));
            System.out.println(MobileLocationUtil.getMobileLocation("13134498066"));
            System.out.println(MobileLocationUtil.getMobileLocation("13508309573"));
            System.out.println(MobileLocationUtil.getMobileLocation("13766162973"));
            System.out.println(MobileLocationUtil.getMobileLocation("13676368340"));
            System.out.println(MobileLocationUtil.getMobileLocation("18905360515"));
            System.out.println(MobileLocationUtil.getMobileLocation("13756712726"));
            System.out.println(MobileLocationUtil.getMobileLocation("18844413139"));
            System.out.println(MobileLocationUtil.getMobileLocation("15604438585"));
            System.out.println(MobileLocationUtil.getMobileLocation("13844108460"));
            System.out.println(MobileLocationUtil.getMobileLocation("18265615656"));
            System.out.println(MobileLocationUtil.getMobileLocation("15689822011"));
            System.out.println(MobileLocationUtil.getMobileLocation("15948550531"));
            System.out.println(MobileLocationUtil.getMobileLocation("13159770277"));
            System.out.println(MobileLocationUtil.getMobileLocation("18744454333"));
            System.out.println(MobileLocationUtil.getMobileLocation("15945050145"));
            System.out.println(MobileLocationUtil.getMobileLocation("15584092333"));
            System.out.println(MobileLocationUtil.getMobileLocation("18323447100"));
            System.out.println(MobileLocationUtil.getMobileLocation("13676620596"));
            System.out.println(MobileLocationUtil.getMobileLocation("15054409496"));
            System.out.println(MobileLocationUtil.getMobileLocation("15908025757"));
            System.out.println(MobileLocationUtil.getMobileLocation("13455673412"));
            System.out.println(MobileLocationUtil.getMobileLocation("18629941195"));
            System.out.println(MobileLocationUtil.getMobileLocation("15966070558"));
            System.out.println(MobileLocationUtil.getMobileLocation("15666561530"));
            System.out.println(MobileLocationUtil.getMobileLocation("15948718201"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
