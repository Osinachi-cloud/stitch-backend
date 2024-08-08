package com.stitch.gateway.test;

import org.springframework.stereotype.Service;

@Service
public class ServTranImpl implements ServTran{


    public Transaction processTranOfficePayload(PostTranOfficePayload postTranOfficePayload){
        validateNonDuplicateTransaction(postTranOfficePayload);

        Transaction transaction = new Transaction();


        return null;
    }

    private void validateNonDuplicateTransaction(PostTranOfficePayload postTranOfficePayload){

        try {

        }catch(Exception e){

        }

    }

}
