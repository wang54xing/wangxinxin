package com.fulaan.new33.controller;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.base.controller.BaseController;
import com.fulaan.new33.dto.paike.N33_JXBDTO;
import com.fulaan.new33.service.N33_TimeCombineService;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/timeCombine")
public class N33_TimeCombineController extends BaseController {
	@Autowired
	private N33_TimeCombineService timeCombineService;

	@RequestMapping("/initTimeCombine")
	@ResponseBody
	public RespObj initTimeCombine(@RequestParam String ciId, @RequestParam String gradeId) {
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			timeCombineService.initTimeCombine(new ObjectId(gradeId), new ObjectId(ciId), getSchoolId());
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}

	@RequestMapping("/getTimeCombineHeadNameList")
	@ResponseBody
	public RespObj getTimeCombineHeadNameList(@RequestParam String ciId, @RequestParam String gradeId,@RequestParam Integer type) {
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			List<Map<String,Object>> result = timeCombineService.getTimeCombineHeadNameList(new ObjectId(gradeId), new ObjectId(ciId), getSchoolId(),type);
			obj.setMessage(result);
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}

	@RequestMapping("/getTimeCombine")
	@ResponseBody
	public RespObj getTimeCombine(@RequestParam String ciId, @RequestParam String gradeId,@RequestParam Integer type) {
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			Map<String,Object> result = timeCombineService.getTimeCombine(new ObjectId(gradeId), new ObjectId(ciId), getSchoolId(),type);
			obj.setMessage(result);
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}

	@RequestMapping("/getTimeCombineInfos")
	@ResponseBody
	public RespObj getTimeCombineInfos(@RequestParam String gradeId, @RequestParam String ciId) {
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			Map<String, Object> result = timeCombineService.getTimeCombineInfos(getSchoolId(), new ObjectId(gradeId), new ObjectId(ciId));
			obj.setMessage(result);
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}

	@RequestMapping("/querySelectJXB")
	@ResponseBody
	public RespObj querySelectJXB(@RequestParam String ciId,@RequestParam String gradeId,@RequestParam String tagId,@RequestParam Integer type,@RequestParam Integer serial) {
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			List<N33_JXBDTO> result = timeCombineService.querySelectJXB(new ObjectId(ciId), new ObjectId(gradeId), new ObjectId(tagId),type,getSchoolId(),serial);
			obj.setMessage(result);
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}

	@RequestMapping("/saveJXB")
	@ResponseBody
	public RespObj saveJXB(@RequestParam String ciId,@RequestParam String gradeId,@RequestParam String tagId,@RequestParam Integer type,@RequestParam Integer serial,@RequestParam String jxbId) {
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			timeCombineService.saveJXB(new ObjectId(ciId), new ObjectId(tagId),type,serial,new ObjectId(jxbId),new ObjectId(gradeId),getSchoolId());
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}

	@RequestMapping("/autoTimeCombSaveJXB")
	@ResponseBody
	public RespObj autoTimeCombSaveJXB(
			@RequestParam String ciId,
			@RequestParam String gradeId
	) {
        RespObj resp = new RespObj(Constant.SUCCESS_CODE, "成功");
        try {
            resp.setMessage(timeCombineService.autoTimeCombSaveJXB(getSchoolId(), new ObjectId(gradeId), new ObjectId(ciId)));
        } catch (Exception e) {
            e.printStackTrace();
            resp = new RespObj(Constant.FAILD_CODE, "失败");
        }
        return resp;
	}

	@RequestMapping("/delJXB")
	@ResponseBody
	public RespObj delJXB(@RequestParam String ciId,@RequestParam String gradeId,@RequestParam String tagId,@RequestParam Integer type,@RequestParam Integer serial) {
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			timeCombineService.delJXB(new ObjectId(ciId), new ObjectId(tagId),type,serial,new ObjectId(gradeId),getSchoolId());
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}

	@ResponseBody
	@RequestMapping("/getTeacherList")
	public List<Map<String,Object>> getTeacherList(@RequestParam String gradeId, @RequestParam String subjectId,@RequestParam String ciId,@RequestParam Integer serial,@RequestParam Integer type) {
		return timeCombineService.getTeacherList(getSchoolId(), new ObjectId(gradeId), new ObjectId(subjectId), new ObjectId(ciId), serial, type);
	}

	/**
	 * 删除教学班老师
	 * @param jxbId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/delJxbTeacher")
	public RespObj delJxbTeacher(@RequestParam String jxbId) {
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			timeCombineService.delJxbTeacher(new ObjectId(jxbId));
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}

	@ResponseBody
	@RequestMapping("/delJxbRoom")
	public RespObj delJxbRoom(@RequestParam String jxbId) {
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			timeCombineService.delJxbRoom(new ObjectId(jxbId));
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}

	@ResponseBody
	@RequestMapping("/getClassRoomList")
	public List<Map<String,Object>> getClassRoomList(@ObjectIdType ObjectId gradeId,@RequestParam Integer serial,@RequestParam Integer type){
		return timeCombineService.getClassRoomList(getSchoolId(),gradeId,serial,type);
	}

	@ResponseBody
	@RequestMapping("/clearColumn")
	public RespObj clearColumn(@RequestParam Integer serial,@RequestParam Integer type,@RequestParam String gradeId,@RequestParam String ciId) {

		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			timeCombineService.clearColumn(serial,type,new ObjectId(gradeId),new ObjectId(ciId),getSchoolId());
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}

	@ResponseBody
	@RequestMapping("/delZuHe")
	public RespObj delZuHe(@RequestParam Integer serial,@RequestParam Integer type,@RequestParam String gradeId,@RequestParam String ciId) {

		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			timeCombineService.delZuHe(serial,type,new ObjectId(gradeId),new ObjectId(ciId),getSchoolId());
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}

	@ResponseBody
	@RequestMapping("/addZuHe")
	public RespObj addZuHe(@RequestParam Integer type,@RequestParam String gradeId,@RequestParam String ciId) {

		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			timeCombineService.addZuHe(type,new ObjectId(gradeId),new ObjectId(ciId),getSchoolId());
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}

	@ResponseBody
	@RequestMapping("/checkZuHeData")
	public RespObj checkZuHeData(@RequestParam String gradeId,@RequestParam String ciId) {
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			obj.setMessage(timeCombineService.checkZuHeData(new ObjectId(gradeId),new ObjectId(ciId),getSchoolId()));
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}

	@ResponseBody
	@RequestMapping("/getTagName")
	public RespObj getTagName(@RequestParam String gradeId,@RequestParam String ciId) {
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			obj.setMessage(timeCombineService.getTagName(new ObjectId(ciId),new ObjectId(gradeId)));
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}

	@ResponseBody
	@RequestMapping("/getJxbStudentCount")
	public RespObj getJxbStudentCount(@RequestParam String jxbId) {
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			obj.setMessage(timeCombineService.getJxbStudentCount(new ObjectId(jxbId)));
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}

	@ResponseBody
	@RequestMapping("/getJxbStudent")
	public RespObj getJxbStudent(@RequestParam String jxbId,@RequestParam String classId,@RequestParam String combinName,@RequestParam String tagId) {
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			obj.setMessage(timeCombineService.getJxbStudent(new ObjectId(jxbId),classId,combinName,tagId));
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}
}
