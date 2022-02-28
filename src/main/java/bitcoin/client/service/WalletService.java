package bitcoin.client.service;

import bitcoin.client.entry.Wallet;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class WalletService {
    static String JSON_PATH = "static/files/AddressBook.json";
    static String GRAPH_PATH = "/static/graph/";
    /**
     * Read json file，return address list
     * @return String
     */
    public static String readFile() {
        String jsonStr = "";
        try {
            File jsonFile = ResourceUtils.getFile("classpath:"+JSON_PATH);
            Reader reader = new InputStreamReader(new FileInputStream(jsonFile),"utf-8");//WalletService.class.getClassLoader().getResourceAsStream(JSON_PATH),
            int ch = 0;
            StringBuffer buffer = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                buffer.append((char) ch);
            }
            reader.close();
            jsonStr = buffer.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * Read json file from JAR，return address list
     * @return String
     */
    public static String readFileFromJar(){
        String jsonStr = "";
        try {
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources(JSON_PATH);
            Resource resource = resources[0];
            InputStream stream = resource.getInputStream();
            StringBuilder buffer = new StringBuilder();
            byte[] bytes = new byte[1024];
            try {
                for (int n; (n = stream.read(bytes)) != -1; ) {
                    buffer.append(new String(bytes, 0, n));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            jsonStr = buffer.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * Get random wallet by tag
     * @return Wallet
     */
    public static Wallet getWallet(String jsonStr,String tag) {
        Map<String,Object> map = JSON.parseObject(jsonStr, Map.class);
        List<Wallet> addressObject = new ArrayList<>();
        for (Map.Entry<String, Object> entry: map.entrySet()) {
            JSONArray value = (JSONArray) entry.getValue();
            if(tag.equalsIgnoreCase(String.valueOf(value.get(0)))){
                Wallet temp = new Wallet();
                temp.setAddressOD(entry.getKey());
                temp.setTagOD(tag);
                temp.setWebNameOD(String.valueOf(value.get(1)));
                temp.setSimilarityOD(0);
                addressObject.add(temp);
            }
        }
        if(!CollectionUtils.isEmpty(addressObject)){
            Random r = new Random();
            int indexOfA = r.nextInt(addressObject.size()-1);
            Wallet result = addressObject.get(indexOfA);
            result.setGraphPathOD(GRAPH_PATH+"AV_"+result.getAddressOD()+".png");
            return result;
        }else{
            Wallet result = new Wallet();
            result.setAddressOD("No address");
            result.setTagOD(tag);
            result.setWebNameOD("No website");
            result.setSimilarityOD(0);
            result.setGraphPathOD("No graph");
            return result;
        }
    }
    /**
     * Get wallet by address
     * @return Wallet
     */
    public static Wallet getWalletByAddress(String jsonStr,String address) {
        Map<String,Object> map = JSON.parseObject(jsonStr, Map.class);
        JSONArray tags = (JSONArray) map.get(address);
        Wallet wallet = new Wallet();
        wallet.setAddressOD(address);
        wallet.setGraphPathOD(GRAPH_PATH+"AV_"+address+".png");
        wallet.setSimilarityOD(0);
        wallet.setTagOD(String.valueOf(tags.get(0)));
        wallet.setWebNameOD(String.valueOf(tags.get(1)));
        return wallet;
    }
}
