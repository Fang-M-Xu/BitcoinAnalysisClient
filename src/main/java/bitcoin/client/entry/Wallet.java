package bitcoin.client.entry;

import lombok.Data;

@Data
public class Wallet {
    /**
     * Address of wallet
     */
    private String addressOD;
    /**
     * the tag of wallet
     */
    private String tagOD;
    /**
     * the name of the web that this wallet belong
     */
    private String webNameOD;
    /**
     * the statistic to fraud
     */
    private String similarityOD;
    /**
     * the graphPath of wallet
     */
    private String graphPathOD;
}
