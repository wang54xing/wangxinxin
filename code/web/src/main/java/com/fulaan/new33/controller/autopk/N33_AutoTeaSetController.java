package com.fulaan.new33.controller.autopk;

import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.fulaan.base.controller.BaseController;
import com.fulaan.new33.service.autopk.N33_AutoTeaSetService;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;

@Controller
@RequestMapping("/autoPk/autoTeaSet")
public class N33_AutoTeaSetController extends BaseController {
	
	@Autowired
	private N33_AutoTeaSetService autoTeaSetService;
	
	@ResponseBody
	@RequestMapping("/getAllTeachers")
	public String getAllTeachers(@RequestBody Map repMap) {
		RespObj resp = new RespObj(Constant.SUCCESS_CODE, "成功");
		try {
			repMap.put("schoolId", getSchoolId());
			resp.setMessage(autoTeaSetService.getAllTeachers(repMap));
		} catch (Exception e) {
			resp = new RespObj(Constant.FAILD_CODE, "失败");
			e.printStackTrace();
		}
		return JSON.toJSONString(resp);
	}

	@ResponseBody
	@RequestMapping("/addOrupdAutoTeaSet")
	public String addOrupdAutoTeaSet(@RequestBody Map repMap) {
		RespObj resp = new RespObj(Constant.SUCCESS_CODE, "成功");
		try {
			repMap.put("schoolId", getSchoolId());
			autoTeaSetService.addOrupdAutoTeaSet(repMap);
		} catch (Exception e) {
			resp = new RespObj(Constant.FAILD_CODE, "失败");
			e.printStackTrace();
		}
		return JSON.toJSONString(resp);
	}
	
	@ResponseBody
	@RequestMapping("/getAutoTeaSet")
	public String getAutoTeaSet(@RequestBody Map repMap) {
		RespObj resp = new RespObj(Constant.SUCCESS_CODE, "成功");
		try {
			repMap.put("schoolId", getSchoolId());
			resp.setMessage(autoTeaSetService.getAutoTeaSet(repMap));
		} catch (Exception e) {
			resp = new RespObj(Constant.FAILD_CODE, "失败");
			e.printStackTrace();
		}
		return JSON.toJSONString(resp);
	}
	
	@ResponseBody
	@RequestMapping("/addOrupdTeaWeek")
	public String addOrupdTeaWeek(@RequestBody Map repMap) {
		RespObj resp = new RespObj(Constant.SUCCESS_CODE, "成功");
		try {
			repMap.put("schoolId", getSchoolId());
			autoTeaSetService.addOrupdTeaWeek(repMap);
		} catch (Exception e) {
			resp = new RespObj(Constant.FAILD_CODE, "失败");
			e.printStackTrace();
		}
		return JSON.toJSONString(resp);
	}
	
	@ResponseBody
	@RequestMapping("/getCTeaList")
	public String getCTeaList(@RequestBody Map repMap) {
		RespObj resp = new RespObj(Constant.SUCCESS_CODE, "成功");
		try {
			repMap.put("schoolId", getSchoolId());
			resp.setMessage(autoTeaSetService.getCTeaList(repMap));
		} catch (Exception e) {
			resp = new RespObj(Constant.FAILD_CODE, "失败");
			e.printStackTrace();
		}
		return JSON.toJSONString(resp);
	}
	
	@ResponseBody
	@RequestMapping("/getDTeachers")
	public String getDTeachers(@RequestBody Map repMap) {
		RespObj resp = new RespObj(Constant.SUCCESS_CODE, "成功");
		try {
			repMap.put("schoolId", getSchoolId());
			resp.setMessage(autoTeaSetService.getDTeachers(repMap));
		} catch (Exception e) {
			resp = new RespObj(Constant.FAILD_CODE, "失败");
			e.printStackTrace();
		}
		return JSON.toJSONString(resp);
	}
	
	@ResponseBody
	@RequestMapping("/getDCTeaList")
	public String getDCTeaList(@RequestBody Map repMap) {
		RespObj resp = new RespObj(Constant.SUCCESS_CODE, "成功");
		try {
			repMap.put("schoolId", getSchoolId());
			resp.setMessage(autoTeaSetService.getDCTeaList(repMap));
		} catch (Exception e) {
			resp = new RespObj(Constant.FAILD_CODE, "失败");
			e.printStackTrace();
		}
		return JSON.toJSONString(resp);
	}
	
	@ResponseBody
	@RequestMapping("/addOrupdTeaDay")
	public String addOrupdTeaDay(@RequestBody Map repMap) {
		RespObj resp = new RespObj(Constant.SUCCESS_CODE, "成功");
		try {
			repMap.put("schoolId", getSchoolId());
			autoTeaSetService.addOrupdTeaDay(repMap);
		} catch (Exception e) {
			resp = new RespObj(Constant.FAILD_CODE, "失败");
			e.printStackTrace();
		}
		return JSON.toJSONString(resp);
	}
	
	@ResponseBody
	@RequestMapping("/getStepTeachers")
	public String getStepTeachers(@RequestBody Map repMap) {
		RespObj resp = new RespObj(Constant.SUCCESS_CODE, "成功");
		try {
			repMap.put("schoolId", getSchoolId());
			resp.setMessage(autoTeaSetService.getStepTeachers(repMap));
		} catch (Exception e) {
			resp = new RespObj(Constant.FAILD_CODE, "失败");
			e.printStackTrace();
		}
		return JSON.toJSONString(resp);
	}
	
	@ResponseBody
	@RequestMapping("/getTeaStep")
	public String getStepCTeachers(@RequestBody Map repMap) {
		RespObj resp = new RespObj(Constant.SUCCESS_CODE, "成功");
		try {
			repMap.put("schoolId", getSchoolId());
			resp.setMessage(autoTeaSetService.getTeaStep(repMap));
		} catch (Exception e) {
			resp = new RespObj(Constant.FAILD_CODE, "失败");
			e.printStackTrace();
		}
		return JSON.toJSONString(resp);
	}
	
	@ResponseBody
	@RequestMapping("/addOrupdTeaStep")
	public String addOrupdTeaStep(@RequestBody Map repMap) {
		RespObj resp = new RespObj(Constant.SUCCESS_CODE, "成功");
		try {
			repMap.put("schoolId", getSchoolId());
			autoTeaSetService.addOrupdTeaStep(repMap);
		} catch (Exception e) {
			resp = new RespObj(Constant.FAILD_CODE, "失败");
			e.printStackTrace();
		}
		return JSON.toJSONString(resp);
	}
	
	@ResponseBody
	@RequestMapping("/getTeaNutex")
	public String getTeaNutex(@RequestBody Map repMap) {
		RespObj resp = new RespObj(Constant.SUCCESS_CODE, "成功");
		try {
			repMap.put("schoolId", getSchoolId());
			resp.setMessage(autoTeaSetService.getTeaNutex(repMap));
		} catch (Exception e) {
			resp = new RespObj(Constant.FAILD_CODE, "失败");
			e.printStackTrace();
		}
		return JSON.toJSONString(resp);
	}
	
	@ResponseBody
	@RequestMapping("/getAllTeas")
	public String getAllTeas(@RequestBody Map repMap) {
		RespObj resp = new RespObj(Constant.SUCCESS_CODE, "成功");
		try {
			repMap.put("schoolId", getSchoolId());
			resp.setMessage(autoTeaSetService.getAllTeas(repMap));
		} catch (Exception e) {
			resp = new RespObj(Constant.FAILD_CODE, "失败");
			e.printStackTrace();
		}
		return JSON.toJSONString(resp);
	}
	
	@ResponseBody
	@RequestMapping("/addTeaMutex")
	public String addOrupdTeaMutex(@RequestBody Map repMap) {
		RespObj resp = new RespObj(Constant.SUCCESS_CODE, "成功");
		try {
			repMap.put("schoolId", getSchoolId());
			autoTeaSetService.addTeaMutex(repMap);
		} catch (Exception e) {
			resp = new RespObj(Constant.FAILD_CODE, "失败");
			e.printStackTrace();
		}
		return JSON.toJSONString(resp);
	}
	
	@ResponseBody
	@RequestMapping("/delTeaMutex")
	public String delTeaMutex(@RequestBody Map repMap) {
		RespObj resp = new RespObj(Constant.SUCCESS_CODE, "成功");
		try {
			repMap.put("schoolId", getSchoolId());
			autoTeaSetService.delTeaMutex(repMap);
		} catch (Exception e) {
			resp = new RespObj(Constant.FAILD_CODE, "失败");
			e.printStackTrace();
		}
		return JSON.toJSONString(resp);
	}
	
	//============================================== 其他设置 ==========================================
	
	@ResponseBody
	@RequestMapping("/addOtherSet")
	public String addOthSet(@RequestBody Map repMap) {
		RespObj resp = new RespObj(Constant.SUCCESS_CODE, "成功");
		try {
			repMap.put("schoolId", getSchoolId());
			autoTeaSetService.addOtherSet(repMap);
		} catch (Exception e) {
			resp = new RespObj(Constant.FAILD_CODE, "失败");
			e.printStackTrace();
		}
		return JSON.toJSONString(resp);
	}
	
	@ResponseBody
	@RequestMapping("/getAutoOtherSet")
	public String getAutoOtherSet(@RequestBody Map repMap) {
		RespObj resp = new RespObj(Constant.SUCCESS_CODE, "成功");
		try {
			repMap.put("schoolId", getSchoolId());
			resp.setMessage(autoTeaSetService.getAutoOtherSet(repMap));
		} catch (Exception e) {
			resp = new RespObj(Constant.FAILD_CODE, "失败");
			e.printStackTrace();
		}
		return JSON.toJSONString(resp);
	}
	
	@RequestMapping("/getSubjectByType")
    @ResponseBody
    public RespObj getSubjectByType(@RequestParam String xqid, @RequestParam String gid, @RequestParam String subType) {
        RespObj obj = new RespObj(Constant.SUCCESS_CODE);
        try {
            obj.setMessage(autoTeaSetService.getSubjectByType(new ObjectId(xqid), new ObjectId(gid), getSchoolId(), subType));
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Constant.FAILD_CODE);
            obj.setMessage("服务器正忙");
        }
        return obj;
    }
	
	@ResponseBody
	@RequestMapping("/getOsubCheks")
	public String getOsubCheks(@RequestBody Map repMap) {
		RespObj resp = new RespObj(Constant.SUCCESS_CODE, "成功");
		try {
			repMap.put("schoolId", getSchoolId());
			resp.setMessage(autoTeaSetService.getOsubCheks(repMap));
		} catch (Exception e) {
			resp = new RespObj(Constant.FAILD_CODE, "失败");
			e.printStackTrace();
		}
		return JSON.toJSONString(resp);
	}
	
	@ResponseBody
	@RequestMapping("/addOsubCheks")
	public String addOsubCheks(@RequestBody Map repMap) {
		RespObj resp = new RespObj(Constant.SUCCESS_CODE, "成功");
		try {
			repMap.put("schoolId", getSchoolId());
			autoTeaSetService.addOsubCheks(repMap);
		} catch (Exception e) {
			resp = new RespObj(Constant.FAILD_CODE, "失败");
			e.printStackTrace();
		}
		return JSON.toJSONString(resp);
	}
	
	@ResponseBody
	@RequestMapping("/addGz")
	public String addGz(@RequestBody Map repMap) {
		RespObj resp = new RespObj(Constant.SUCCESS_CODE, "成功");
		try {
			repMap.put("schoolId", getSchoolId());
			autoTeaSetService.addGz(repMap);
		} catch (Exception e) {
			resp = new RespObj(Constant.FAILD_CODE, "失败");
			e.printStackTrace();
		}
		return JSON.toJSONString(resp);
	}
	
	@ResponseBody
	@RequestMapping("/getGzs")
	public String getGzs(@RequestBody Map repMap) {
		RespObj resp = new RespObj(Constant.SUCCESS_CODE, "成功");
		try {
			repMap.put("schoolId", getSchoolId());
			resp.setMessage(autoTeaSetService.getGzs(repMap));
		} catch (Exception e) {
			resp = new RespObj(Constant.FAILD_CODE, "失败");
			e.printStackTrace();
		}
		return JSON.toJSONString(resp);
	}
}
