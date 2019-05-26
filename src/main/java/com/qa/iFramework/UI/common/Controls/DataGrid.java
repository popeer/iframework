package com.qa.iFramework.UI.common.Controls;

import com.qa.iFramework.UI.common.Elements.ElementBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Map;

public class DataGrid {
    static final String defaultGridContainer = "div[role='grid']";
    static final String defaultHeaderRepeater = "col in colContainer.renderedColumns track by col.uid";
    static final String defaultRowRepeater = "(rowRenderIndex, row) in rowContainer.renderedRows track by $index";

    // String defaultColumnRepeater1 = "(colRenderIndex, col) in colContainer.renderedColumns track by col.uid";
    // String defaultColumnRepeater2 = "(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name";
    static final String defaultColumnRepeater3 = "(colRenderIndex, col) in colContainer.renderedColumns";
    private static By ByNoDataExist = By.cssSelector("p[ng-show='isNoData']");

    private static By ByDefaultSelectedRow = By.cssSelector("div.ui-grid-row.ng-scope.ui-grid-row-selected");
    private static By ByExpandActionMenuIcon = By.cssSelector("i.icon_nb_action_menu");

    private static By BySortNoneIcon = By.cssSelector("i.ui-grid-icon-blank");
    private static By BySortASCIcon = By.cssSelector("i.ui-grid-icon-up-dir");
    private static By BySortDESCIcon = By.cssSelector("i.ui-grid-icon-down-dir");

    public static Object CommonAction;

    public static WebElement getGrid(String path){
        List<WebElement> dataGrids = null;
        try{
            dataGrids = ElementBase.FindElements(By.xpath(path), 10);
            WebElement finalGrid = null;
            if (0 == dataGrids.size()){
                return finalGrid;
            }
            return dataGrids.get(dataGrids.size() - 1);

        } catch (Exception ex){
            System.out.println(ex);
        }
        return null;
    }

    /*
    新增商品时商品的各个价格的GRID
     */
    public static boolean inputPriceGridValue(WebElement element, Map<String,String> map) throws Exception{
        List<WebElement> cells = element.findElements(By.xpath(".//div[@data-dom-key='CG-CELL']"));
        Thread.sleep(1000);

        for(Map.Entry<String,String> entry :map.entrySet()){
            for(WebElement we : cells) {
                String fieldName = we.getAttribute("data-col-field");
                System.out.println(fieldName);
                if (fieldName.toLowerCase().trim().equals(entry.getKey().trim().toLowerCase())) {
                    we.click();
                    Thread.sleep(2000);
                    WebElement inputElement = ElementBase.findElement(By.className("cg-editor-wrapper"), 10, false);
                    WebElement input = inputElement.findElement(By.xpath(".//input"));
                    input.sendKeys(entry.getValue());
                    System.out.println("click done " + cells.indexOf(we));
                }
            }
        }

        element.click();
        System.out.println("click done");

        return true;
    }

    /// <summary>
    /// 获取当前行的集合
    /// </summary>
    /// <param name="ByGridContainerCssSelector"></param>
    /// <returns></returns>
    public static List<WebElement> GetAllRows(String ByGridContainerCssSelector)
    {
//        if (ByGridContainerCssSelector == defaultGridContainer)
//        {
//            return ElementBase.FindElements(NgBy.repeater(defaultRowRepeater));
//        }
//        else
//        {
//            return ElementBase.findElement(By.xpath(ByGridContainerCssSelector), 10, false).findElements(NgBy.repeater(defaultRowRepeater));
        List<WebElement> dataGrid = null;
        try{
            dataGrid = ElementBase.FindElements(By.xpath(ByGridContainerCssSelector), 10);
            if(dataGrid.size() == 2){
                WebElement element = dataGrid.get(1);
                List<WebElement> cells = element.findElements(By.xpath(".//div[@data-dom-key='CG-CELL']"));

                Thread.sleep(1000);
                for(WebElement we : cells){
                    we.click();
                    Thread.sleep(2000);
                    WebElement inputElement = ElementBase.findElement(By.className("cg-editor-wrapper"), 10, false);
                    WebElement input = inputElement.findElement(By.xpath(".//input"));
                    input.sendKeys("22");

                    System.out.println("click done " + cells.indexOf(we));
                }


                System.out.println("click done");
            }

        }catch (Exception ex){
            System.out.println(ex);
            try{
                WebElement element = dataGrid.get(1);
                List<WebElement> cells = element.findElements(By.xpath("//div[@data-dom-key='CG-CELL']"));
                System.out.println();
            }catch (Exception ex2){

            }

        }
        return null;
//        }
    }

//    public static bool IsNoDataAvailableShow()
//    {
//        return Control.IsDisplayed(ByNoDataExist);
//    }

    /// <summary>
    /// 获取数据表的一行
    /// </summary>
    /// <param name="findByCellValue">查找依据单元格的值</param>
    /// <returns></returns>
    public static WebElement GetOneRow(String findByCellValue, String ByGridContainerCssSelector)
    {
        List<WebElement> rows = GetAllRows(ByGridContainerCssSelector);
        for (WebElement row : rows)
        {
            if (row.getText().contains(findByCellValue))
            {
                return row;
            }
        }
        return null;
    }

    /// <summary>
    /// 获取数据表的一行
    /// </summary>
    /// <param name="columnIndex">列索引</param>
    /// <param name="findByCellValue">查找依据单元格的值</param>
    /// <returns></returns>
    public static WebElement GetOneRow(int columnIndex, String findByCellValue, String ByGridContainerCssSelector)
    {
        List<WebElement> rows = GetAllRows(ByGridContainerCssSelector);
        for (WebElement row : rows)
        {
            List<WebElement> cells = row.findElements(By.cssSelector("div[ng-repeat*='" + defaultColumnRepeater3 + "']"));
            if (cells.get(columnIndex).getText().contains(findByCellValue))
            {
                return row;
            }
        }
        return null;
    }

    /// <summary>
    /// 获取数据表的一行
    /// </summary>
    /// <param name="columnIndex">列索引</param>
    /// <param name="findByCellValue">查找依据单元格的值</param>
    /// <returns></returns>
    public static WebElement GetOneRowByIndex(int rowIndex, String ByGridContainerCssSelector)
    {
        List<WebElement> rows = GetAllRows(ByGridContainerCssSelector);

        if (rows.size() > 0)
        {
            return rows.get(rowIndex);
        }
        else
        {
            return null;
        }
    }

    /// <summary>
    /// 获取数据表的一单元格
    /// </summary>
    /// <param name="columnIndex">列索引</param>
    /// <param name="findByCellValue">查找依据单元格的值</param>
    /// <returns></returns>
    public static WebElement GetOneCell(int columnIndex, String findByCellValue, String ByGridContainerCssSelector)
    {
        WebElement row = GetOneRow(findByCellValue, ByGridContainerCssSelector);
        if (row != null)
        {
            //int s = row.FindElements(NgBy.Repeater(columnRepeater)).Count;
            return row.findElements(By.cssSelector("div[ng-repeat*='" + defaultColumnRepeater3 + "']")).get(columnIndex);
        }
        else
        {
            return null;
        }
    }

//    /// <summary>
//    /// 根据行列索引获取某一个单元格。
//    /// </summary>
//    /// <param name="rowIndex"></param>
//    /// <param name="columnIndex"></param>
//    /// <param name="ByGridContainerCssSelector"></param>
//    /// <returns></returns>
//    public static NgWebElement GetOneCell(int rowIndex, int columnIndex, String ByGridContainerCssSelector = defaultGridContainer)
//    {
//        List<NgWebElement> rows = GetAllRows(defaultGridContainer).ToList();
//        if (rows.Count > 0)
//        {
//            return rows[rowIndex].FindElements(By.CssSelector("div[ng-repeat*='" + defaultColumnRepeater3 + "']"))[columnIndex];
//        }
//        return null;
//    }
//
//    /// <summary>
//    /// 获取目标行的索引
//    /// </summary>
//    /// <param name="columnIndex">列索引</param>
//    /// <param name="findByCellValue">查找依据单元格的值</param>
//    /// <returns></returns>
//    public static int GetRowIndex(int columnIndex, String findByCellValue, String ByGridContainerCssSelector = defaultGridContainer)
//    {
//        List<NgWebElement> rows = GetAllRows(ByGridContainerCssSelector);
//        int index = -1;
//        int i = 0;
//        foreach (NgWebElement row in rows)
//        {
//            List<NgWebElement> cells = row.FindElements(By.CssSelector("div[ng-repeat*='" + defaultColumnRepeater3 + "']"));
//            if (cells.ToArray()[columnIndex].Text.Contains(findByCellValue))
//            {
//                index = i;
//                break;
//            }
//            i++;
//        }
//        return index;
//    }
//
//    /// <summary>
//    /// 通过列名获取列索引
//    /// </summary>
//    /// <param name="columnName"></param>
//    /// <returns></returns>
//    public static int GetColumnIndex(String columnName, String ByGridContainerCssSelector = defaultGridContainer)
//    {
//        List<NgWebElement> headers = null;
//
//        headers = ElementBase.findElement(By.CssSelector(ByGridContainerCssSelector)).FindElements(NgBy.Repeater(defaultHeaderRepeater));
//        int index = -1;
//        int i = 0;
//        foreach (var head in headers)
//        {
//            if (head.Text.Trim().Equals(columnName))
//            {
//                index = i;
//                break;
//            }
//            i++;
//        }
//        return index;
//    }
//
//    /// <summary>
//    /// 获取一列的值
//    /// </summary>
//    /// <param name="columnIndex"></param>
//    /// <param name="ByGridContainerCssSelector"></param>
//    /// <returns></returns>
//    public static List<String> GetOneColumnValues(int columnIndex, String ByGridContainerCssSelector = defaultGridContainer)
//    {
//        List<String> columns = new List<String>();
//        List<NgWebElement> rows = GetAllRows(ByGridContainerCssSelector);
//        foreach (NgWebElement row in rows)
//        {
//            if (row.Text != "")
//            {
//                columns.Add(row.FindElements(By.CssSelector("div[ng-repeat*='" + defaultColumnRepeater3 + "']"))[columnIndex].Text);
//            }
//        }
//        return columns;
//    }
//
//    /// <summary>
//    /// 获取一列的值
//    /// </summary>
//    /// <param name="columnName"></param>
//    /// <param name="ByGridContainerCssSelector"></param>
//    /// <returns></returns>
//    public static List<String> GetOneColumnValues(String columnName, String ByGridContainerCssSelector = defaultGridContainer)
//    {
//        int columnIndex = GetColumnIndex(columnName, ByGridContainerCssSelector);
//        return GetOneColumnValues(columnIndex, ByGridContainerCssSelector);
//    }
//
//    /// <summary>
//    /// 仅提供数据表格的Container即可以获取整张表格的数据
//    /// </summary>
//    /// <param name="ByGridContainerCssSelector"></param>
//    /// <returns></returns>
//    public static DataTable GetEntireDataGridValues(String ByGridContainerCssSelector = defaultGridContainer)
//    {
//        DataTable grid = new DataTable();
//            #region Get Headers and fill the dataTable
//        List<String> headers = GetGridHeaders(ByGridContainerCssSelector);
//
//        for (int i = 0; i < headers.Count; i++)
//        {
//            grid.Columns.Add(headers[i]);
//        }
//            #endregion
//
//            #region
//
//        List<NgWebElement> rows = GetAllRows(ByGridContainerCssSelector);
//        foreach (var row in rows)
//        {
//            DataRow gridRow = grid.NewRow();
//            List<NgWebElement> cells = row.FindElements(By.CssSelector("div[ng-repeat*='" + defaultColumnRepeater3 + "']")).ToList();
//            if (cells.Count > 0)
//            {
//                for (int i = 0; i < cells.Count; i++)
//                {
//                    gridRow[i] = cells[i].Text;
//                }
//                grid.Rows.Add(gridRow);
//            }
//        }
//            #endregion
//
//        return grid;
//    }
//
//        #endregion
//
//        #region Methods base on Selected Row
//    /// <summary>
//    /// 当数据行被选定时，我们可以获取到被选定的数据行
//    /// </summary>
//    /// <returns></returns>
//    public static NgWebElement GetSelectedRow()
//    {
//        if (ElementBase.FindElements(ByDefaultSelectedRow).Count > 0)
//        {
//            foreach (var e in ElementBase.FindElements(ByDefaultSelectedRow))
//            {
//                if (e.Displayed)
//                { return ElementBase.findElement(ByDefaultSelectedRow); }
//            }
//        }
//        return null;
//    }
//
//    /// <summary>
//    /// 当数据行被选定时，我们可以获取到被选定的数据行
//    /// </summary>
//    /// <returns></returns>
//    public static List<String> GetSelectedRowData()
//    {
//        List<String> rowCellValues = new List<String>();
//        NgWebElement selectedRow = GetSelectedRow();
//        List<NgWebElement> cells = selectedRow.FindElements(By.CssSelector("div[ng-repeat*='" + defaultColumnRepeater3 + "']"));
//        foreach (var e in cells)
//        {
//            rowCellValues.Add(e.Text);
//        }
//        return rowCellValues;
//    }
//
//    /// <summary>
//    /// 通过展开图标展开Action 菜单
//    /// </summary>
//    public static void ExpandActionMenu_ClickIcon()
//    {
//        foreach (var e in ElementBase.FindElements(ByExpandActionMenuIcon))
//        {
//            if (e.Displayed)
//            {
//                Button.Click(e);
//                break;
//            }
//        }
//    }
//
//    /// <summary>
//    /// 通过右键点击某一行展开Action 菜单
//    /// </summary>
//    public static void ExpandActionMenu_RightClickRow()
//    {
//        Control.RightClick(GetSelectedRow());
//    }
//
//    /// <summary>
//    /// 从Action菜单中选择一项。
//    /// </summary>
//    /// <param name="actionName"></param>
//    public static void SelectAction(String actionName, String actionRepeater= "action in row.entity.$menu")
//    {
//        foreach (var action in ElementBase.FindElements(NgBy.Repeater(actionRepeater)))
//        {
//            if (action.Text.Contains(actionName))
//            {
//                action.Click();
//                break;
//            }
//        }
//    }
//
//    /// <summary>
//    /// 获取Action菜单中的操作名称集合
//    /// </summary>
//    /// <returns></returns>
//    public static List<String> GetActions()
//    {
//        List<String> actions = new List<String>();
//        foreach (var a in ElementBase.FindElements(NgBy.Repeater("item in rowActionMenuList")))
//        {
//            actions.Add(a.Text);
//        }
//        return actions;
//    }
//
//    /// <summary>
//    /// 连续操作： 右击选定的行，然后选择操作菜单项
//    /// </summary>
//    /// <param name="actionName"></param>
//    public static void RightClickSelectedRowAndSelectAction(String actionName) throws Exception
//    {
//        ExpandActionMenu_RightClickRow();
//        Thread.sleep(500);
//        SelectAction(actionName);
//        //foreach (var action in ElementBase.FindElements(NgBy.Repeater("item in rowContextMenuList")))
//        //{
//        //    if (action.Text.Contains(actionName))
//        //    {
//        //        action.Click();
//        //        break;
//        //    }
//        //}
//    }
//
//    /// <summary>
//    /// 连续操作： 右击选定的行，然后选择操作菜单项, 部分子菜单中只有一个选项时，repeater不是默认的需要传入。
//    /// </summary>
//    /// <param name="actionName"></param>
//    public static void RightClickSelectedRowAndSelectAction(String actionName, String ByRepeater)
//    {
//        ExpandActionMenu_RightClickRow();
//        Thread.Sleep(500);
//        SelectAction(actionName, ByRepeater);
//
//    }
//
//    /// <summary>
//    /// 连续操作： 展开选定行的菜单，然后选择操作菜单项
//    /// </summary>
//    /// <param name="actionName"></param>
//    public static void ExpandAndSelectActionOnSelectedRow(String actionName, String actionrepeater="action in row.entity.$menu")
//    {
//        ExpandActionMenu_ClickIcon();
//        SelectAction(actionName,actionrepeater);
//    }
//
//
//        // 带滚动条的DataGrid公共方法,仅处理左右滚动条
//
//    public static DataTable GetRows_HorizontalScrollBar(String JS_Element, String ByGridContainerCssSelector = defaultGridContainer)
//    {
//        DataTable Result = new DataTable();
//        List<DataTable> DTList = new List<DataTable>();
//        NgWebDriver Driver = Browser.Browser.NgDriver;
//        //Get DataRable Container
//        NgWebElement e = ElementBase.findElement(By.CssSelector(ByGridContainerCssSelector));
//
//        try
//        {
//            //获取指定控件/元素的整体宽度
//            int totalWidth = Convert.ToInt32(((IJavaScriptExecutor)Driver.WrappedDriver).ExecuteScript("var e=" + JS_Element + ";return e.scrollWidth").ToString());
//
//            // 获取指定控件/元素在可视的窗口高度和宽度
//            int viewWidth = Convert.ToInt32(((IJavaScriptExecutor)Driver).ExecuteScript("var e=" + JS_Element + ";return e.clientWidth").ToString());
//            int viewHeight = Convert.ToInt32(((IJavaScriptExecutor)Driver).ExecuteScript("return window.innerHeight"));
//
//
//            List<Rectangle> rectangleList = new List<Rectangle>();
//
//            // 宽度处理：循环滚动直到达到页面最右侧
//            for (int ii = 0; ii < totalWidth; ii += viewWidth)
//            {
//                int newWidth = viewWidth;
//                //最后一个区域的处理
//                if (ii + viewWidth > totalWidth)
//                {
//                    newWidth = totalWidth - ii;
//                }
//
//                // 创建局部区域
//                Rectangle currRect = new Rectangle(ii, viewHeight, newWidth, viewHeight);
//                rectangleList.Add(currRect);
//            }
//
//            // 拼接DataTable 去重行和列
//            Rectangle previous = Rectangle.Empty;
//            foreach (var rectangle in rectangleList)
//            {
//                if (previous != Rectangle.Empty)
//                {
//                    int xDiff = rectangle.Right - previous.Right;
//
//                    //滚动滚动条，View下一区域
//                    ((IJavaScriptExecutor)Driver).ExecuteScript(String.Format("var e=" + JS_Element + "; e.scrollBy({0}, {1})", xDiff, 0));
//                    System.Threading.Thread.Sleep(500);
//                }
//
//                // 获取当前表格数据
//                DataTable dt = GetEntireDataGridValues(ByGridContainerCssSelector);
//                DTList.Add(dt);
//                previous = rectangle;
//            }
//        }
//        catch { }
//
//            #region 拼接多个DataTable到一个DataTable
//
//        Result = DTList[0];
//        if (DTList.Count > 1)
//        {
//            for (int i = 1; i < DTList.Count; i++)
//            {
//                Result = MergeDataTable.LeftJoin_DataTable(Result, DTList[i]);
//            }
//
//            //Result = MergeDataTable.LeftJoin_DataTable(DTList[0],DTList[1]);
//        }
//            #endregion
//        return Result;
//    }
//
//        #endregion
//
//        #region 仅处理上下滚动条
//
//    public static DataTable GetRows_VerticalScrollBar(String JS_Element, String ByGridContainerCssSelector = defaultGridContainer)
//    {
//        DataTable Result = new DataTable();
//        List<DataTable> DTList = new List<DataTable>();
//        NgWebDriver Driver = Browser.Browser.NgDriver;
//        //Get DataRable Container
//        NgWebElement e = ElementBase.findElement(By.CssSelector(ByGridContainerCssSelector));
//
//        try
//        {
//            //获取指定控件/元素的整体高度
//            int totalHeight = Convert.ToInt32(((IJavaScriptExecutor)Driver.WrappedDriver).ExecuteScript("var e=" + JS_Element + ";return e.scrollHeight").ToString());
//
//            // 获取指定控件/元素在可视的窗口高度和宽度
//            int viewWidth = Convert.ToInt32(((IJavaScriptExecutor)Driver).ExecuteScript("var e=" + JS_Element + ";return e.clientWidth").ToString());
//            int viewHeight = Convert.ToInt32(((IJavaScriptExecutor)Driver).ExecuteScript("var e=" + JS_Element + ";return e.clientHeight").ToString());
//
//
//            List<Rectangle> rectangleList = new List<Rectangle>();
//
//            // 高度处理：循环滚动直到达到页面最底部
//            for (int ii = 0; ii < totalHeight; ii += viewHeight)
//            {
//                int newHeight = viewHeight;
//                //最后一个区域的处理
//                if (ii + viewHeight > totalHeight)
//                {
//                    newHeight = totalHeight - ii;
//                }
//
//                // 创建局部区域
//                Rectangle currRect = new Rectangle(viewWidth, ii, viewWidth, newHeight);
//                rectangleList.Add(currRect);
//            }
//
//            Rectangle previous = Rectangle.Empty;
//            foreach (var rectangle in rectangleList)
//            {
//                if (previous != Rectangle.Empty)
//                {
//                    int yDiff = rectangle.Bottom - previous.Bottom;
//
//                    //滚动滚动条，View下一区域
//                    ((IJavaScriptExecutor)Driver).ExecuteScript(String.Format("var e=" + JS_Element + "; e.scrollBy({0}, {1})", 0, yDiff));
//                    System.Threading.Thread.Sleep(500);
//                }
//
//                // 获取当前表格数据
//                DataTable dt = GetEntireDataGridValues(ByGridContainerCssSelector);
//                DTList.Add(dt);
//                previous = rectangle;
//            }
//        }
//        catch { }
//
//            // 拼接多个DataTable到一个DataTable,删除重复行
//
//        Result = DTList[0];
//        if (DTList.Count > 1)
//        {
//            for (int i = 1; i < DTList.Count; i++)
//            {
//                Result = MergeDataTable.MergeRows_DataTable(Result, DTList[i]);
//            }
//        }
//
//        return Result;
//    }
}
