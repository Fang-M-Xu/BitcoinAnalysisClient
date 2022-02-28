package bitcoin.client.controller;

import bitcoin.client.entry.Wallet;
import bitcoin.client.service.WalletService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/statistic")
public class StatisticController {
    String DEFAULT_TAG = "exchanges";
    String DEFAULT_ADD = "11HgtNU2XSzDvEcfqyu2yafrmGdiGsnAr";
    String DEFAULT_WEBNAME = "Bitcurex.com";
    String DEFAULT_GRAPH = "/static/graph/AV_11HgtNU2XSzDvEcfqyu2yafrmGdiGsnAr.png";
    Integer DEFAULT_SIMI = 0;

    @GetMapping("/index")
    public String index(ModelMap modelMap){
        modelMap.put("tagOD",DEFAULT_TAG);
        modelMap.put("webNameOD",DEFAULT_WEBNAME);
        modelMap.put("similarityOD",DEFAULT_SIMI);
        modelMap.put("addressOD",DEFAULT_ADD);
        modelMap.put("graphPathOD", DEFAULT_GRAPH);
        return "statisticWallet";
    }

    @PostMapping("/getWallet")
    @ResponseBody
    public Wallet getWallet(HttpServletRequest request){
        String tag = request.getParameter("filterTag");
        /*get wallet info*/
        String bookJsonStr = WalletService.readFile("BOOK");
        /*get prediction of wallet*/
        String predictionJsonStr = WalletService.readFile("PREDICT");
        Wallet walletObj = WalletService.getWallet(bookJsonStr,predictionJsonStr,tag);
        System.out.println(walletObj.getAddressOD());
        return walletObj;
    }

    @GetMapping("/getWalletByAddress/{address}")
    public String getWalletByAddress(@PathVariable("address") String address,ModelMap modelMap){
        /*get wallet info*/
        String bookJsonStr = WalletService.readFile("BOOK");
        /*get prediction of wallet*/
        String predictionJsonStr = WalletService.readFile("PREDICT");
        Wallet walletObj = WalletService.getWalletByAddress(bookJsonStr,predictionJsonStr,address);
        modelMap.put("tagOD",walletObj.getTagOD());
        modelMap.put("webNameOD",walletObj.getWebNameOD());
        modelMap.put("similarityOD",walletObj.getSimilarityOD());
        modelMap.put("addressOD",walletObj.getAddressOD());
        modelMap.put("graphPathOD", walletObj.getGraphPathOD());
        return "statisticWallet";
    }

}
