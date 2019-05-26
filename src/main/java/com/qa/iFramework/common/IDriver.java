package com.qa.iFramework.common;

import com.qa.iFramework.common.IDataManager;
import com.qa.iFramework.common.IVerifier;
import com.qa.iFramework.common.processor.CommandEntity;

import java.io.File;
import java.util.List;

/**
 * Created by haijia on 11/21/16.
 */
public interface IDriver {
    String Execute(List<File> files, IVerifier verifier, IDataManager dataManager, CommandEntity commandEntity);
}
