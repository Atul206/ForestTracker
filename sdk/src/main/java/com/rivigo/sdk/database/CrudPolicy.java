package com.rivigo.sdk.database;

import java.util.ArrayList;

/**
 * Created by atulsakhala on 19/02/18.
 */

public interface CrudPolicy {

    void addPolicy(PolicyModel response);

    PolicyModel getLatestPolicy();

    PolicyModel getLatestPolicyByLanguage(String languageCode);

    ArrayList<PolicyModel> getPolicyList();

    PolicyModel getPolicyById(long id);
}
