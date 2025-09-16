package br.com.gwent.engine.services;

import br.com.gwent.engine.services.executor.GameActionExecuterTest;
import br.com.gwent.engine.services.flow.GameFlowManagerTest;
import br.com.gwent.engine.services.validator.GameValidatorTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

    @Suite
    @SelectClasses({
            GameActionExecuterTest.class,
            GameFlowManagerTest.class,
            GameValidatorTest.class
    })
    public class GwentCoreTestSuit {
    }
