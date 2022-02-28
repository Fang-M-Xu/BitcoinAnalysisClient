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
import java.util.*;

import static java.lang.String.valueOf;

public class WalletService {
    static String BOOK_PATH = "static/files/AddressBookForWeb.json";
    static String PREDICT_PATH = "static/files/AddressPredictions.json";
    static String GRAPH_PATH = "/static/graph/";

    /**
     * Read json file from JAR，return address list
     * @return String
     */
    public static String readFile(String fileType){
        String jsonStr = "";
        try {
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = new Resource[0];
            if("BOOK".equals(fileType)){
                resources = resolver.getResources(BOOK_PATH);
            }else if("PREDICT".equals(fileType)){
                resources = resolver.getResources(PREDICT_PATH);
            }
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
    public static Wallet getWallet(String bookJsonStr,String predictionJsonStr,String tag) {
        Map<String,Object> bookMap = JSON.parseObject(bookJsonStr, Map.class);
        List<Wallet> addressObject = new ArrayList<>();
        Map<String,Object> predictionMap = JSON.parseObject(predictionJsonStr, Map.class);

        for (Map.Entry<String, Object> entry: bookMap.entrySet()) {
            JSONArray value = (JSONArray) entry.getValue();
            if(value.size()>1){
                for(int i =0;i<value.size();i++){
                    if(tag.equalsIgnoreCase(valueOf(value.get(i)))){
                        Wallet temp = new Wallet();
                        temp.setAddressOD(entry.getKey());
                        temp.setTagOD(tag);
                        if(i+1<value.size()){
                            temp.setWebNameOD(valueOf(value.get(i+1)));
                        }else{
                            temp.setWebNameOD(valueOf(value.get(1)));
                        }
                        temp.setSimilarityOD(valueOf(predictionMap.get(entry.getKey())));
                        addressObject.add(temp);
                    }
                }
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
            result.setAddressOD("——");
            result.setTagOD(tag);
            result.setWebNameOD("——");
            result.setSimilarityOD("——");
            result.setGraphPathOD("——");
            return result;
        }
    }
    /**
     * Get wallet by address
     * @return Wallet
     */
    public static Wallet getWalletByAddress(String bookJsonStr,String predictionJsonStr,String address) {
        Map<String,Object> bookMap = JSON.parseObject(bookJsonStr, Map.class);
        JSONArray tags = (JSONArray) bookMap.get(address);
        Map<String,Object> predictionMap = JSON.parseObject(predictionJsonStr, Map.class);

        Wallet wallet = new Wallet();
        wallet.setAddressOD(address);
        if(Objects.isNull(tags)){
            wallet.setSimilarityOD("——");
            wallet.setTagOD("——");
            wallet.setWebNameOD("——");
            wallet.setGraphPathOD("——");
        }else{
            wallet.setSimilarityOD(valueOf(predictionMap.get(address)));
            wallet.setTagOD(valueOf(tags.get(0)));
            wallet.setWebNameOD(valueOf(tags.get(1)));
            wallet.setGraphPathOD(GRAPH_PATH+"AV_"+address+".png");
        }
        return wallet;
    }

//    public static void main(String[] args) {
//        File file2 = new File("/static/graph/AV_1nGkTEppUm22Yhcv7rLE9bVBKrmjMDqvs.png");
//        System.out.println(file2.exists());
//        try{
//            File file = ResourceUtils.getFile("classpath:"+"static/graph/AV_1nGkTEppUm22Yhcv7rLE9bVBKrmjMDqvs.png");
//            System.out.println(file.exists());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
