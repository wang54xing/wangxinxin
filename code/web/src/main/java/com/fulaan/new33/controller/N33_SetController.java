package com.fulaan.new33.controller;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.base.controller.BaseController;
import com.fulaan.new33.dto.isolate.N33_DefaultTermDTO;
import com.fulaan.new33.dto.isolate.N33_ManagerDTO;
import com.fulaan.new33.dto.isolate.TermDTO;
import com.fulaan.new33.service.isolate.*;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/n33_set")
public class N33_SetController extends BaseController{
	
	@Autowired
	private N33_SWService swService;
	
	@Autowired
	private N33_DefaultTermService termService;
	
	@Autowired
	private IsolateUserService userService;
	
	@Autowired
	private IsolateSubjectService subjectService;

	@Resource
	private IsolateTermService isolateTermService;

	@Autowired
	private N33_TurnOffService turnOffService;
	
    /**
     * 增加多条事务类型
     * @param
     * @return
     */
   /* @RequestMapping("/addSWType")
    @ResponseBody
    public RespObj addSWType(@RequestBody List<N33_SWDTO> swdtoList) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            swService.addSwEntryList(swdtoList);
            obj.setMessage("新增成功");
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }*/
	
	@RequestMapping("/addSWTypeDto")
	@ResponseBody
	public RespObj addSWTypeDto(@RequestParam String desc,@RequestParam String id,@RequestParam String xqid){
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			if("*".equals(id)){
				swService.addSwlbDto(desc,getSchoolId(),new ObjectId(xqid));
			}else{
				swService.updateSwlbDto(new ObjectId(id),desc);
			}
			obj.setMessage("新增成功");
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}
	
	/**
	 * 逻辑删除事务类别
	 * @param 
	 * @return
	 */
	@RequestMapping("/removeSWType")
	@ResponseBody
	public RespObj removeSWType(@RequestParam String id) {
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			swService.removeSwlbDto(new ObjectId(id));
			obj.setMessage("删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}
	
	/**
	 * 选择默认学期
	 * @param 
	 * @return
	 */
	@RequestMapping("/updateDefaultTerm")
	@ResponseBody
	public RespObj updateDefaultTerm(@RequestBody N33_DefaultTermDTO dto) {
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			dto.setSchoolId(getSchoolId().toString());
			termService.updateDefaultTerm(dto);
			obj.setMessage("选择成功");
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}
	
	/**
	 * 查询默认学期
	 * @param 
	 * @return
	 */
	@RequestMapping("/getDefaultTerm")
	@ResponseBody
	public RespObj getDefaultTerm() {
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			obj.setMessage(termService.getDefaultTerm(getSchoolId()));
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}

	/**
	 * 根据管理者姓名进行模糊查询
	 * @param 
	 * @return
	 */
	@RequestMapping("/getManagerList")
	@ResponseBody
	public RespObj getManagerList(@RequestParam String userName) {
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			obj.setMessage(userService.getTeaListByName(getSchoolId(), userName));
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}
	
	/**
	 * 增加管理员
	 * @param dto
	 * @return
	 */
	@RequestMapping("/addManager")
	@ResponseBody
	public RespObj addManager(@RequestBody N33_ManagerDTO dto){
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			if("*".equals(dto.getId())){
				dto.setId(new ObjectId().toString());
			}
			dto.setSchoolId(getSchoolId().toString());
			swService.addManagerDto(dto);
			obj.setMessage("添加成功");
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}
	

	/**
	 * 删除管理员
	 * @param 
	 * @return
	 */
	@RequestMapping("/deleteManager")
	@ResponseBody
	public RespObj deleteManager(@RequestParam String id) {
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			swService.deleteManager(new ObjectId(id));
			obj.setMessage("删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}
	
	/**
	 * 查询所有的管理员
	 * @param 
	 * @return
	 */
	@RequestMapping("/getAllManagerList")
	@ResponseBody
	public RespObj getAllManagerList() {
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			obj.setMessage(swService.getAllManagerList(getSchoolId()));
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}

	/**
	 * 查询可能走班的学科
	 */
	@RequestMapping("/getSubjectCanZouBan")
	@ResponseBody
	public RespObj getSubjectCanZouBan(@RequestParam String xqid,@RequestParam String gid){
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			obj.setMessage(subjectService.getSubjectCanZouBan(getSchoolId(),new ObjectId(xqid),new ObjectId(gid)));
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}


	/**
	 * 查询可能走班的学科
	 */
	@RequestMapping("/getAllZouBanSubject")
	@ResponseBody
	public RespObj getCanZBSubject(@RequestParam String xqid){
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			obj.setMessage(subjectService.getIsolateSubjectBySchoolId(getSchoolId(),new ObjectId(xqid)));
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}

	/**
	 * 总体设置走班学科
	 */
	@RequestMapping("/setIsZouBanList")
	@ResponseBody
	public RespObj setIsZouBanList(@RequestBody List<Map> maps){
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			subjectService.setIsZouBanList(maps,getSchoolId());
			obj.setMessage("设置成功");
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}
	
	/**
	 * 设置走班学科
	 */
	@RequestMapping("/setIsZouBan")
	@ResponseBody
	public RespObj setIsZouBan(@RequestParam String subId,@RequestParam String gid,@RequestParam Integer isZouBan,@RequestParam String xqid){
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			subjectService.setIsZouBan(new ObjectId(subId),new ObjectId(gid),isZouBan,getSchoolId(),new ObjectId(xqid));
			obj.setMessage("设置成功");
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}

	@RequestMapping("/getPaikeTimesByTermId")
	@ResponseBody
	public RespObj getPaikeTimesByTermId(@ObjectIdType ObjectId termId){
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			List<Map<String,Object>> list = isolateTermService.getPaikeTimesByTermId(termId, getSchoolId());
			obj.setMessage(list);
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage(e.getMessage());
		}
		return obj;
	}

	@RequestMapping("/addPaikeTime")
	@ResponseBody
	public RespObj addPaikeTime(@ObjectIdType ObjectId termId,@RequestBody TermDTO.PaiKeTimeDTO dto){
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			isolateTermService.addPaikeTime(getSchoolId(), termId, dto);
			obj.setMessage("成功");
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage(e.getMessage());
		}
		return obj;
	}

	@RequestMapping("/updatePaikeTime")
	@ResponseBody
	public RespObj updatePaikeTime(@ObjectIdType ObjectId termId,@ObjectIdType ObjectId timeId,@RequestBody TermDTO.PaiKeTimeDTO dto){
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			isolateTermService.updatePaikeTime(termId,timeId,dto);
			obj.setMessage("成功");
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage(e.getMessage());
		}
		return obj;
	}

	@RequestMapping("/removePaiKeTime")
	@ResponseBody
	public RespObj removePaiKeTime(@ObjectIdType ObjectId termId,@ObjectIdType ObjectId timeId){
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			isolateTermService.removePaiKeTime(termId,timeId);
			obj.setMessage("成功");
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage(e.getMessage());
		}
		return obj;
	}
	@RequestMapping("/updatePaiKeTerm")
	@ResponseBody
	public RespObj updatePaiKeTerm(@ObjectIdType ObjectId paikeTermId,
								   @ObjectIdType ObjectId paikeciId){
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			termService.updatePaiKeTerm(getSchoolId(),paikeTermId,paikeciId);
			obj.setMessage("成功");
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage(e.getMessage());
		}
		return obj;
	}

	@RequestMapping("/updateTurnOff")
	@ResponseBody
	public RespObj updateTurnOff(@RequestParam String ciId,
	                               @RequestParam String gradeId,@RequestParam Integer status,@RequestParam Integer type){
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			turnOffService.updateTurnOff(new ObjectId(gradeId),new ObjectId(ciId),getSchoolId(),status,type);
			obj.setMessage("设置成功");
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage(e.getMessage());
		}
		return obj;
	}

	@RequestMapping("/getTurnOff")
	@ResponseBody
	public RespObj getTurnOff(@RequestParam String ciId,
	                             @RequestParam String gradeId,@RequestParam Integer type){
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			obj.setMessage(turnOffService.getTurnOff(new ObjectId(gradeId),new ObjectId(ciId),getSchoolId(),type));
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage(e.getMessage());
		}
		return obj;
	}

	@RequestMapping("/updateAcClassToTurnOff")
	@ResponseBody
	public RespObj updateAcClassToTurnOff(@RequestParam String ciId,
							  @RequestParam String gradeId,@RequestParam Integer type,@RequestParam Integer acClass){
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			turnOffService.updateAcClassToTurnOff(new ObjectId(gradeId),new ObjectId(ciId),getSchoolId(),type,acClass);
			obj.setMessage("修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage(e.getMessage());
		}
		return obj;
	}

	@RequestMapping("/saveDESCForNoCourse")
	@ResponseBody
	public RespObj saveDESCForNoCourse(@RequestParam String ciId,
	                                   @RequestParam String gradeId,@RequestParam Integer type,@RequestParam String desc,@RequestParam Integer kbType){
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			turnOffService.saveDESCForNoCourse(new ObjectId(gradeId),new ObjectId(ciId),getSchoolId(),type,desc,kbType);
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage(e.getMessage());
		}
		return obj;
	}

	@RequestMapping("/getDESCForNoCourse")
	@ResponseBody
	public RespObj getDESCForNoCourse(@RequestParam String ciId,
	                                   @RequestParam String gradeId,@RequestParam Integer type,@RequestParam Integer kbType){
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			String s = turnOffService.getDESCForNoCourse(new ObjectId(gradeId),new ObjectId(ciId),getSchoolId(),type,kbType);
			obj.setMessage(s);
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage(e.getMessage());
		}
		return obj;
	}


	@RequestMapping("/getAcClassType")
	@ResponseBody
	public RespObj getAcClassType(
		  @RequestParam String gradeId,
		  @RequestParam String ciId
	){
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			int acClassType = turnOffService.getAcClassType(getSchoolId(), new ObjectId(gradeId), new ObjectId(ciId), 1);
			obj.setMessage(acClassType);
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage(e.getMessage());
		}
		return obj;
	}

}
