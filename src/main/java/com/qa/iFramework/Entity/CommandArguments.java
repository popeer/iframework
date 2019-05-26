package com.qa.iFramework.Entity;

import com.qa.iFramework.common.Util.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by haijia on 11/30/16.
 */
public class CommandArguments {
    private static Logger log = LogManager.getLogger(CommandArguments.class);
    public static String NumericTestCasePrefix = "TestCase";
    public static String CaseID;
    public static String Suite;
    public static String SuiteFile;
    public static String RunName = "TestRun";
    public static boolean Distributed;
    public static boolean Export;
    public static boolean ListAllCases;
    public static boolean LoopUntilFail;
    public static String ServerList;
    public static Map<String, String> _parameters;  // keys are case-insensitive
    public static String ArchivePath;
    public static String SeqNum;
    public static int RunTimes = 1;
    public static int ExitOnStep = 0;
    public static boolean ExitOnFail;

    private static String HELP_STRING = "\n" +
            "            -------------------------------------------------------------------------------\n" +
            "    Invoker\n" +
            "    Purpose:  Invokes one or more test cases to be run:\n" +
            "            -------------------------------------------------------------------------------\n" +
            "    Usage:\n" +
            "            -case|-c <TestCaseID>\n" +
            "        [-assembly|-as <AssemblyName>]\n" +
            "            [-archive|-ar <path>] -params|-p [param1=value1];[param2=value2]\n" +
            "            [-seqnum|-sn <SequenceNumber>]\n" +
            "            [-runTimes|-rt <number>] [-exitOnFail|-ef]\n" +
            "            [-exitOnStep|-es <number>]\n" +
            "    or\n" +
            "        -assembly|-as <AssemblyName> -list|-l \n" +
            "        [-runTimes|-rt <number>] [-exitOnFail|-ef]\n" +
            "    or\n" +
            "        -suite|-su <TestCaseNode> \n" +
            "        [-runName|-r <RunName>]\n" +
            "            [-archive|-ar <path>] -params|-p [param1=value1];[param2=value2]\n" +
            "            [-distributed|-d]\n" +
            "            [-servers|-se <ServerList>]\n" +
            "            [-runTimes|-rt <number>] [-exitOnFail|-ef]\n" +
            "    or\n" +
            "        -suiteFile|-sf <SuiteFileName> \n" +
            "        -suite|-su <TestCaseNode> \n" +
            "        [-runName|-r <RunName>]\n" +
            "            [-archive|-ar <path>] -params|-p [param1=value1];[param2=value2]\n" +
            "            [-distributed|-d]\n" +
            "            [-servers|-se <ServerList>]\n" +
            "            [-runTimes|-rt <number>] [-exitOnFail|-ef]\n" +
            "    or\n" +
            "        -export|-ex <TestCaseNode> <SuiteFileName>\n" +
            "\n" +
            "    or\n" +
            "        -?\n" +
            "\n" +
            "    Parameter Definition:\n" +
            "    TestCaseID:  It can be either:\n" +
            "            - The class name of the test case\n" +
            "            - The integer ID of the test case.  In that case invoker will look \n" +
            "        for a class name in the following\n" +
            "            format: \" + NumericTestCasePrefix + @\"<TestCaseID>.\n" +
            "    For example: \"\"\" + NumericTestCasePrefix + @\"101\"\"\n" +
            "            - The name of the <TestCaseID>.test.config file containing the\n" +
            "    test case definiton\n" +
            "            SequenceNumber\n" +
            "    Sequence number(s) of test case(s) to run. This number is assigned to\n" +
            "    a test case when invoker is run with the -list option. Can be a\n" +
            "    single number or combination of comma-delimited numbers or range of\n" +
            "    numbers. For example: '1,3,5' or '1-5,10-15'. The sequence number\n" +
            "    assocated with a test case may change if test cases are added or\n" +
            "    removed, or if build order of source files change.\n" +
            "    AssemblyName\n" +
            "    The name of the assembly to load the test case from.  If it is not\n" +
            "    specified then all assemblies in the current directory and its\n" +
            "    subdirectories will be searched recursively starting with\n" +
            "    subfolder 'TestCases'. The path is relative to invoker.Exe\n" +
            "    directory.  If a list is not specified then all test cases\n" +
            "    from the assembly are run\n" +
            "\n" +
            "            List\n" +
            "    If specified then all test cases will be listed in the assembly\n" +
            "    loopuntilfail\n" +
            "    Continues to rerun the test case or test cases until a failure occurs\n" +
            "    servers CommaDelimitedList\n" +
            "    If -distributed and -servers are specified then test cases will be\n" +
            "    run on all the machines specified.  Machines have to have the\n" +
            "    NAS.Remoter.exe running on them in order for this to work\n" +
            "    RunName\n" +
            "    Descriptive name for the test run to be stored in the log.\n" +
            "            SuiteFileName\n" +
            "    Name of and XML file containing a list of test cases\n" +
            "            TestCaseNode\n" +
            "    A path to the TCM suite containing test cases to run.\n" +
            "    If the list of test cases is defined using SuiteFileName\n" +
            "    then the top-level group id is the suite\n" +
            "    Params\n" +
            "    A semicolon delimited list of parameters to pass to the test cases\n" +
            "    where each parameter is enclosed in brackets. Parameters must be in\n" +
            "    the form [param=value] where param is the name of the parameter and\n" +
            "    value is the value to use. You can use parameters to override any\n" +
            "    String contained in the config file.  Another valid format is\n" +
            "    {param=value}. Within the value part, the enclosing brackets can be\n" +
            "    included by escaping with a double, as in [param=start[[middle]]end].\n" +
            "    Archive <path>\n" +
            "    Saves the results of each test case to the given path.\n" +
            "    If the parameter is not specified then test results will be\n" +
            "    placed into the \" + _archivePath + @\" folder\n" +
            "    RunTimes <number>\n" +
            "    The number of times to run a set of tests. This is applicable to\n" +
            "    single cases as well as suites and assemblies.\n" +
            "    ExitOnFail\n" +
            "    Causes execution of test cases to stop immediately after a failed case.\n" +
            "    ExitOnStep\n" +
            "    Exits the test case when the specified repro step occurs. We will exit\n" +
            "    before the repro step is run.\n" +
            "\n" +
            "    Examples:\n" +
            "    Invoke a TestCase1 test case class from the assembly NAS.XYZ.dll\n" +
            "        >Invoker -case TestCase1\n" +
            "        >Invoker -case 1 -assembly NAS.XYZ.dll\n" +
            "        >Invoker -c TestCase1 -as NAS.XYZ.dll\n" +
            "        >Invoker -c TestCase1 -as NAS.XYZ.dll -params [Language=3081]\n" +
            "    List all test cases from the assembly NAS.XYZ.dll\n" +
            "        >Invoker -assembly NAS.XYZ.dll -list\n" +
            "    Invoke all test cases from the assembly NAS.XYZ.dll, archive to the\n" +
            "    custom log location\n" +
            "        >Invoker -assembly NAS.XYZ.dll -ar TestLogResuits\\TodayLogs\n" +
            "    RunHttpPost all test cases from the sample suite file\n" +
            "        >Invoker -suite TestRun -suitefile SampleTestCaseConfig.xml\n" +
            "    RunHttpPost TestCase1 from assembly NAS.XYZ.dll continually until it fails\n" +
            "        >Invoker -case TestCase1 -assembly NAS.XYZ.dll -loopuntilfail\n" +
            "        >Invoker -c TestCase1 -as NAS.XYZ.dll -loop";

    /// <summary>
    /// Creates a table based on the parameters defined
    /// </summary>
    /// <param name="args">String array containing a list of arguments</param>
    /// <param name="startIndex">The index of the params in the array</param>
    /// <param name="succeeded">True if the table creation succeeded</param>
    private static void CreateParamsTable(String[] args, int startIndex, boolean succeeded)
    {

    }

    /// <summary>
    /// Assigns a value to a String that is located after the starter index
    /// verifies that there is an item in the array that is that long
    /// </summary>
    /// <param name="args">String array containing a list of arguments</param>
    /// <param name="startIndex">an index in the array after which to try to assign value</param>
    /// <param name="succeeded">true if assignment succeeds</param>
    /// <returns>value to assign</returns>
    private static String AssignParamValue(String[] args, int startIndex, boolean succeeded)
    {
        return AssignParamValue(args, startIndex, null, succeeded);
    }
    /// <summary>
    /// Assigns a value to a String that is located after the starter index
    /// verifies that there is an item in the array that is that long
    /// </summary>
    /// <param name="args">String array containing a list of arguments</param>
    /// <param name="startIndex">an index in the array after which to try to assign value</param>
    /// <param name="messageIfFail">A message to outut to console if the attempt fails</param>
    /// <param name="succeeded">true if assignment succeeds</param>
    /// <returns>value to assign</returns>
    private static String AssignParamValue(String[] args, int startIndex, String messageIfFail, boolean succeeded)
    {
        String param;
        succeeded = true;
        if (args.length > startIndex + 1) // if the index is within bounds
            param = args[startIndex + 1];
        else
        {
            succeeded = false;
            param = null;
            if (!StringUtils.isEmptyOrSpace(messageIfFail))
            {
                log.error(messageIfFail);
            }
        }
        return param;
    }

    private static int AssignParamValueToInt(String[] args, int startIndex, String messageIfFail, boolean succeeded)
    {
        int val = 0;
        String strVal = AssignParamValue(args, startIndex, messageIfFail, succeeded);

        if (succeeded)
        {
            succeeded = false;
            if (!StringUtils.isEmptyOrSpace(messageIfFail))
                log.error(messageIfFail);
            return Integer.parseInt(strVal);
        }

        return val;
    }

    /// <summary>
    /// Parses the command line output and sets all of the input variables
    /// </summary>
    /// <param name="args">list of arguments from the command line</param>
    /// <returns>True if parsing succeeds, false otherwise</returns>
    public static boolean Parse(String[] args)
    {
        _parameters = new HashMap<String, String>() {
        };
        boolean succeeded = true;
        if (null == args || args.length == 0)
        {
            succeeded = false;
        }
        else
        {
            // Loops for all arguments until we are done or find a failure
            for (int i = 0; succeeded && i < args.length; i++)
            {
                String normalizedArgument = args[i].toLowerCase();

                switch (normalizedArgument)
                {
                    case "-case":
                    case "-c":
                        CaseID = AssignParamValue(args, i++, "Error: Test case name is not specified", succeeded);
                        break;
                    case "-suite":
                    case "-su":
                        Suite = AssignParamValue(args, i++, "Error: TestCaseNode name is not speficied", succeeded);
                        break;
                    case "-suitefile":
                    case "-sf":
                        SuiteFile = AssignParamValue(args, i++, "Error: TestCaseNode file is not specified", succeeded);
                        break;
                    case "-distributed":
                    case "-d":
                        Distributed = true;
                        break;
                    case "-loopuntilfail":
                    case "-loop":
                        if (false == ListAllCases)
                        {
                            // Set ExitOnFail to true as well, so we stop when a test case fails
                            ExitOnFail = true;
                            LoopUntilFail = true;
                        }
                        break;
                    case "-list":
                    case "-l":
                        ListAllCases = true;
                        ExitOnFail = false;
                        LoopUntilFail = false;
                        break;
                    case "-params":
                    case "-p":
                        CreateParamsTable(args, i++, succeeded);
                        break;
                    case "-servers":
                    case "-se":
                        ServerList = AssignParamValue(args, i++, "Error: Server list is not speficied", succeeded);
                        break;
                    case "-runname":
                    case "-r":
                        RunName = AssignParamValue(args, i++, "Error: name of the run is not specified", succeeded);
                        break;
                    case "-export":
                    case "-ex":
                        Export = true;
                        //note that if 1st fail then the second will fail as well
                        Suite = AssignParamValue(args, i++, "Error: TestCaseNode name is not specified", succeeded);
                        SuiteFile = AssignParamValue(args, i++, "Error: TestCaseNode file is not specified", succeeded);
                        break;
                    case "-archive":
                    case "-ar":
                        ArchivePath = AssignParamValue(args, i++, "Error: archive path is not specified", succeeded);
                        break;
                    case "-seqnum":
                    case "-sn":
                        SeqNum = AssignParamValue(args, i++, "Error: Sequence number is not specified", succeeded);
                        break;
                    case "-runtimes":
                    case "-rt":
                        RunTimes = AssignParamValueToInt(args, i++, "Error: RunTimes is not specified or is not a number", succeeded);
                        break;
                    case "-exitonfail":
                    case "-ef":
                        ExitOnFail = true;
                        break;
                    case "-exitonstep":
                    case "-es":
                        ExitOnStep = AssignParamValueToInt(args, i++, "Error: ExitOnStep is not specified or is not a number", succeeded); ;
                        break;
                    case "?":
                    case "/?":
                    case "-?":
                        succeeded = false;
                        break;
                    default:
                        succeeded = false;
                        log.error("Error: Unknown input parameter " + args[i]);
                        break;
                }//case
            }//for
        }// if not null
        if (!succeeded)
        {
            log.error(HELP_STRING);
        }
        return succeeded;

    }

    /// <summary>
    /// If input is String then it returns it unchanged
    /// If it is convertable to an integer then we prefixe it with the NumericTestCasePrefix
    /// </summary>
    /// <param name="testCaseID">The test case name</param>
    /// <returns>if testCaseID is int then it returns NumericTestCasePrefix+testCaseID</returns>
    static String NormalizeTestName(String testCaseID)
    {
        if (!StringUtils.isEmptyOrSpace(testCaseID))
        {
            testCaseID = NumericTestCasePrefix + testCaseID;
        }
        return testCaseID;
    }
}
