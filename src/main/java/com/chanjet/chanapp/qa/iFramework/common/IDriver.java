package com.chanjet.chanapp.qa.iFramework.common;

import com.chanjet.chanapp.qa.iFramework.common.IDataManager;
import com.chanjet.chanapp.qa.iFramework.common.IVerifier;
import com.chanjet.chanapp.qa.iFramework.common.processor.CommandEntity;

import java.io.File;
import java.util.List;

/**
 * Created by haijia on 11/21/16.
 */
public interface IDriver {
    String Execute(List<File> files, IVerifier verifier, IDataManager dataManager, CommandEntity commandEntity);
}
