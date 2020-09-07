package com.nanjing.controller;
import com.nanjing.entity.PageResult;
import com.nanjing.entity.Result;
import com.nanjing.pojo.People;
import com.nanjing.service.PeopleService;
import com.nanjing.util.ExcelUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Response;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.List;

@RestController
@Api(tags = "管理相关接口")
@RequestMapping("/people")
public class PeopleController {
    @Autowired
    private PeopleService peopleService;

    /**
     * 用户
     * @param
     * @return
     */
    @PostMapping("/login")
    @ApiOperation("用户登陆的接口")
    public Boolean userLogin(String pname,String countryid){
        try {
            if (peopleService.userLogin(pname,countryid).getPid()!=null){
                return true;
            }else {
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    /***
     * 获取全部用户信息
     * @return
     */
    @GetMapping("/findAll")
    @ApiOperation("查询所有用户的接口")
    public List<People> getPeopleList() {
        return peopleService.getUserList();
    }

    /**
     * 分页查询
     */
   /* @RequestMapping("/findPage")
    public PageResult findPage(int page, int rows) {
        return peopleService.findPage( page, rows );
    }
*/
    @PostMapping("/search")
    @ApiOperation("分页查询接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页", dataType = "int",defaultValue = "1"),
            @ApiImplicitParam(name = "rows", value = "条数", dataType = "int",defaultValue = "10"),
    })
    public PageResult search(@RequestBody People people, int page, int rows) {
        return peopleService.findPage(people, page, rows );
    }

    /***
     * 新增用户
     * @return
     */
    @PostMapping("/add")
    @ApiOperation("添加用户的接口")
    public Result createPeople(@RequestBody People people) {
        try {
            peopleService.createUser(people);
            return new Result(true,"添加成功！！！");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"添加失败！，请重试！！");
        }
    }

    /***
     * 获取指定id用户信息
     * @param pid
     * @return
     */

    @GetMapping("/getOne")
    @ApiOperation("查询用户的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pid", value = "用户id")
    })
    public People getPeople(Long pid) {

        return peopleService.getUser(pid);
    }

    /**
     * 修改用户信息
     * @param people
     * @return
     */
    @PutMapping("/edit")
    @ApiOperation("修改用户的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pid", value = "用户id")
    })
    public Boolean updatePeople(@RequestBody People people) {
      try{
          peopleService.updateUser(people);
        return true;
    }catch (Exception e){
        return false;
    }
    }

    /***
     * 删除指定id用户
     * @param selectIds
     * @return
     */
    @DeleteMapping("/delete")
    @ApiOperation("删除用户的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "selectIds", value = "用户id集合",dataType = "Integer",allowMultiple = true, required = true )

    })
    public Boolean deletePeople( Long [] selectIds) {
        try {
            for (Long selectId : selectIds) {
                peopleService.deleteUser(selectId);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //导出功能
    @GetMapping("/export")
    @ApiOperation("导出excl")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "selectIds", value = "用户id集合",dataType = "Integer",allowMultiple = true, required = true ),
            @ApiImplicitParam(name = "request", value = "request请求"),
            @ApiImplicitParam(name = "response", value = "response相应")

    })
    public Boolean peopleExport(Long [] selectIds, HttpServletRequest request, HttpServletResponse response) throws IOException {
        //按照一组ID查询一组数据
        List<People> list = peopleService.queryCustomerByIds(selectIds);


        //excel标题
        String[] title = {"编号",  "姓名", "ID号码", "添加时间"};

        //excel文件名
        String fileName = "客户信息表" + System.currentTimeMillis() + ".xls";

        //sheet名
        String sheetName = "客户信息表";
        String[][] content = new String[list.size()][];
        for (int i = 0; i < list.size(); i++) {
            content[i] = new String[title.length];
            People obj = list.get(i);
            content[i][0] = obj.getPid() + "";
            content[i][1] = obj.getPname() + "";
            content[i][2] = obj.getCountryid() + "";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String format = sdf.format(obj.getCreatetime());
            content[i][3] = format;
        }

        //创建HSSFWorkbook
        HSSFWorkbook wb = ExcelUtils.getHSSFWorkbook(sheetName, title, content, null);

        //响应到客户端
        try {
            this.setResponseHeader(response, fileName);
            OutputStream os = response.getOutputStream();
            //直接下载给用户，服务器没有保存
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    //发送响应流方法
    public void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
                fileName = new String(fileName.getBytes(),"ISO8859-1");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
            response.setContentType("application/vnd.ms-excel");
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
//pdf在线预览
    @RequestMapping("/view")
    public void er(HttpServletResponse response){
        File file = new File("C:\\Users\\Admin\\Desktop\\发票.pdf");
        if (file.exists()){
            byte[] data = null;
            try {
                FileInputStream input = new FileInputStream(file);
                data = new byte[input.available()];
                input.read(data);
                response.getOutputStream().write(data);
                input.close();
            } catch (Exception e) {
                System.out.println(e);
            }

        }else{
            return;
        }

    }
}