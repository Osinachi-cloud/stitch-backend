package com.stitch.wallet.service;

import com.stitch.wallet.model.dto.BaseWalletCreditRequest;
import com.stitch.wallet.model.dto.BaseWalletDebitRequest;
import com.stitch.wallet.model.dto.WalletDebitReversalRequest;


public interface BaseWalletService {

    void creditInflowBaseWallet(BaseWalletCreditRequest creditRequest);

    void creditOutflowBaseWallet(BaseWalletCreditRequest creditRequest);

    void debitInflowBaseWallet(BaseWalletDebitRequest debitRequest);

    void debitOutflowBaseWallet(BaseWalletDebitRequest debitRequest);

    void reverseOutflowBaseWalletTransaction(WalletDebitReversalRequest debitReversalRequest);
}
