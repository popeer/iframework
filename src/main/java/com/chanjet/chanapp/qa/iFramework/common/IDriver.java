package com.chanjet.chanapp.qa.iFramework.common;

import com.chanjet.chanapp.qa.iFramework.common.processor.CommandEntity;

/**
 * Created by haijia on 11/21/16.
 */
public interface IDriver {
    String Execute(String path, IVerifier verifier, IDataManager dataManager, CommandEntity commandEntity);
}
