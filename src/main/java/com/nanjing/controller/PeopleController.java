package com.nanjing.controller;
import cn.com.infosec.netsign.agent.NetSignAgent;
import cn.com.infosec.netsign.agent.NetSignResult;
import cn.com.infosec.netsign.agent.exception.NetSignAgentException;
import cn.com.infosec.netsign.agent.exception.ServerProcessException;
import cn.com.infosec.netsign.base.PDFParameters;
import cn.com.infosec.netsign.base.PDFVerifyResult;
import com.alibaba.fastjson.JSONObject;
import com.nanjing.entity.CountOrder;
import com.nanjing.entity.PageResult;
import com.nanjing.entity.Program;
import com.nanjing.entity.Result;
import com.nanjing.pojo.People;
import com.nanjing.service.PeopleService;
import com.nanjing.util.ExcelUtils;

import com.nanjing.util.NumberToCNUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.mosmith.tools.report.engine.output.ReportHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

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
    @GetMapping("/view")
    @ApiOperation("pdf预览")
    public void er(HttpServletResponse response){
        File file = new File("C:\\Users\\Admin\\Desktop\\图片\\3.gif");
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

    @RequestMapping(value = "/pdf",method = RequestMethod.GET)
    @ApiOperation("pdf生成")
    @ResponseBody
    public void getPdf(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Reader templateDataReader=new FileReader(new File("D:\\idea-workspace\\pdfsign\\src\\main\\resources\\static\\11.xmreport"));
        CountOrder countOrder =new CountOrder();
        countOrder.setCoName("李白");
        countOrder.setCreateTime(format.format(new Date()));
        countOrder.setEndTime(format.format(new Date()));
        countOrder.setAcctfileno("0000000213124832");
        countOrder.setDate(format.format(new Date()));
        countOrder.setMaker("猪八戒");
        countOrder.setChecker("孙悟空");
        List<Program> list=new ArrayList();
        Program program1 =new Program();
        program1.setAmt(new BigDecimal(100).setScale(2));
        program1.setPro("住院费");
        Program program2 =new Program();
        program2.setAmt(new BigDecimal(200).setScale(2));
        program2.setPro("挂号费");
        Program program3 =new Program();
        program3.setAmt(new BigDecimal(300).setScale(2));
        program3.setPro("x光线费");
        list.add(program1);
        list.add(program2);
        list.add(program3);
        BigDecimal sum =new BigDecimal(0.00);
        for (Program o : list) {
            sum=sum.add(o.getAmt());
        }
        Map<String,List> map =new HashMap<>();
        map.put("program",list);
        countOrder.setProgram(JSONObject.toJSON(map));
        System.out.println(sum.setScale(2));
        countOrder.setCountMoney(sum.setScale(2));
        countOrder.setCountAmt(NumberToCNUtils.number2CNMontrayUnit(countOrder.getCountMoney()));
        Map<String,Object> previewOptions=new HashMap<String, Object>(); // 配置
        ReportHelper reportHelper=new ReportHelper();
        String contentType;
        File file=reportHelper.toPdf(templateDataReader,countOrder, previewOptions);
        try {
            FileInputStream in = new FileInputStream(file);
            NetSignAgent.initialize();
            byte[] bs = new byte[in.available()];
            in.read(bs);
// 设置签名参数.
            PDFParameters para = new PDFParameters();
// 设置pdf文件.
            para.setPdf(bs);
// 设置pdf文件密码.
            para.setOwnerPassword("11111111".getBytes());
            in = new FileInputStream("D:\\idea-workspace\\pdfsign\\src\\main\\resources\\static\\1.jpg");
            bs = new byte[in.available()];
            in.read(bs);
//直接量定位签名.
            para.addRectangle2Sign(100, 450, 200, 550, 1, "fieldName_d", "1.png", "0333001000024109");
            //  para.addImage(bs, 90, 90, 1, 80, 80);
            NetSignResult result = NetSignAgent.pdfSignature(para);
//签名成功，获取签名值
            bs = result.getByteArrayResult(NetSignResult.SIGN_TEXT);
            httpServletResponse.reset();
            httpServletResponse.setContentType("application/pdf");
            httpServletResponse.setCharacterEncoding("utf-8");
            httpServletResponse.setHeader("Content-disposition", "fileName=" + "123.pdf");
            check(bs);
            OutputStream os = httpServletResponse.getOutputStream();
            os.write(bs);
            os.flush();
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            if(file!=null) {
                boolean deleted=file.delete();
            }
        }
    }
    public void check(byte[] bs){
        //  FileInputStream in = null;
        SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        Date date = new Date();
        try {
            //  in = new FileInputStream( "1.pdf" );
            //   byte[] bs = new byte[ in.available() ];
            //   in.read( bs );
            // 设置pdf签名参数
            PDFParameters para = new PDFParameters();
            // 设置pdf文件内容
            para.setPdf( bs );
            ArrayList results = NetSignAgent.pdfVerify( para , true );
            for( int i = 0 , size = results.size() ; i < size ; i++ ) {
                PDFVerifyResult pvr = ( PDFVerifyResult ) results.get( i );
                System.out.println( "签名区域名称:" + pvr.getFieldName() );
                if( pvr.getReturnCode() != 1 ) {
                    System.out.println( "验签名失败：" + pvr.getReturnCode() );
                } else {
                    date.setTime( pvr.getNotBefore()*1000 );
                    String notBefore = format.format( date );
                    date.setTime( pvr.getNotAfter()*1000 );
                    String notAfter = format.format( date );
                    System.out.println( "签名证书主题:" + pvr.getSubject() );
                    System.out.println( "签名证书颁发者主题:" + pvr.getIssuerSubject() );
                    System.out.println( "签名证书序列号:" + pvr.getSn().toString( 16 ) );
                    System.out.println( "签名证书有效期:从" + notBefore + " 到 " + notAfter );
                    System.out.println( "签名证书:" + pvr.getB64cert() );
                }
            }
        } catch ( NetSignAgentException e ) {
            e.printStackTrace();
            System.out.println( "errorCode:" + e.getErrorCode() );
            System.out.println( "errorMsg:" + e.getMessage() );
        } catch ( ServerProcessException e ) {
            e.printStackTrace();
            System.out.println( "errorCode:" + e.getErrorCode() );
            System.out.println( "errorMsg:" + e.getMessage() );
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }


}