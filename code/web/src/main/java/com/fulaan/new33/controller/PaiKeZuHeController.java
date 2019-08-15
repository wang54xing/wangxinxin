package com.fulaan.new33.controller;

import com.fulaan.annotation.UserDataCollection;
import com.fulaan.base.controller.BaseController;
import com.fulaan.new33.dto.N33_PaiKeZuHeDTO;
import com.fulaan.new33.dto.paike.N33_JXBDTO;
import com.fulaan.new33.service.N33_AutoPaiKeByGroupService;
import com.fulaan.new33.service.N33_PaiKeZuHeService;
import com.sys.constants.Constant;
import com.sys.utils.RespObj;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/n33PaikeZuHe")
public class PaiKeZuHeController extends BaseController {
	@Autowired
	private N33_PaiKeZuHeService paiKeZuHeService;

	@Autowired
	private N33_AutoPaiKeByGroupService groupService;

	@UserDataCollection(pageTag = "n33", params = {"gradeId","zuHeType"}, isAllCollection = false)
	@RequestMapping("/getPaiKeZuHeList")
	@ResponseBody
	public RespObj getRoomJXBCount(@RequestParam String ciId, @RequestParam String gradeId, @RequestParam Integer zuHeType) {
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			List<N33_PaiKeZuHeDTO> list = paiKeZuHeService.getPaiKeZuHeList(getSchoolId(),new ObjectId(gradeId),new ObjectId(ciId),zuHeType);
			obj.setMessage(list);
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage(0);
		}
		return obj;
	}

	@RequestMapping("/delEntry")
	@ResponseBody
	public RespObj delEntry(@RequestParam String id) {
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			paiKeZuHeService.delEntry(new ObjectId(id));
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}

	@RequestMapping("/delJxbById")
	@ResponseBody
	public RespObj delJxbById(@RequestParam String id,@RequestParam String jxbId) {
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			paiKeZuHeService.delJxbById(new ObjectId(id), new ObjectId(jxbId));
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}

	@RequestMapping("/addDto")
	@ResponseBody
	public RespObj addDto(@RequestBody N33_PaiKeZuHeDTO dto) {
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			dto.setSchoolId(getSchoolId().toString());
			paiKeZuHeService.addEntry(dto);
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}

	@RequestMapping("/updateDto")
	@ResponseBody
	public RespObj updateDto(@RequestBody Map<String,Object> map) {
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			paiKeZuHeService.updateEntry(map);
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}

	@RequestMapping("/getNoCTJXB")
	@ResponseBody
	public RespObj getNoCTJXB(@RequestBody Map<String,Object> map) {
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			map.put("schoolId",getSchoolId().toString());
			obj.setMessage(paiKeZuHeService.getNoCTJXB(map));
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}

	@UserDataCollection(pageTag = "n33", params = {"gradeId","zuHeType"}, isAllCollection = false)
	@RequestMapping("/getJXBZuHeList")
	@ResponseBody
	public RespObj getJXBZuHeList(@RequestParam String ciId,@RequestParam String gradeId,@RequestParam Integer zuHeType) {
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			obj.setMessage(paiKeZuHeService.getJXBZuHeList(new ObjectId(ciId), getSchoolId(), new ObjectId(gradeId), zuHeType, null));
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}

	@RequestMapping("/saveJXBZuHeList")
	@ResponseBody
	public RespObj saveJXBZuHeList(@RequestParam String ciId,@RequestParam String gradeId,@RequestParam Integer type) {
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			paiKeZuHeService.saveJXBZuHeList(new ObjectId(ciId),getSchoolId(),new ObjectId(gradeId),type);
			obj.setMessage("保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}

	@RequestMapping("/getJXBList")
	@ResponseBody
	public RespObj getJXBList(@RequestParam String ciId,@RequestParam String gradeId,@RequestParam Integer type) {
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			obj.setMessage(paiKeZuHeService.getJXBList(new ObjectId(ciId),getSchoolId(),new ObjectId(gradeId),type));
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}

	@RequestMapping("/getBuFenCTJXB")
	@ResponseBody
	public RespObj getBuFenCTJXB(@RequestParam String jxbId,@RequestParam String gradeId,@RequestParam String ciId) {
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			obj.setMessage(paiKeZuHeService.getBuFenCTJXB(new ObjectId(jxbId), new ObjectId(ciId), getSchoolId(), new ObjectId(gradeId)));
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}


	@RequestMapping("/getZuHeNoCTJXB")
	@ResponseBody
	public RespObj getZuHeNoCTJXB(@RequestParam String jxbId,@RequestParam String name,@RequestParam String ciId,@RequestParam String gradeId,@RequestParam Integer type) {
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			obj.setMessage(paiKeZuHeService.getZuHeNoCTJXB(new ObjectId(jxbId), name, new ObjectId(ciId), getSchoolId(), new ObjectId(gradeId), type));
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}

	@RequestMapping("exportZuHe")
	@ResponseBody
	public void exportZuHe(@RequestParam String ciId,@RequestParam String gradeId,@RequestParam Integer type, @RequestParam String count,@RequestParam String count1, HttpServletResponse response) {
		try {
			paiKeZuHeService.exportZuHe(new ObjectId(ciId),getSchoolId(),new ObjectId(gradeId), type, count, count1, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("/getGradeWeek")
	@ResponseBody
	public RespObj getGradeWeek(@RequestParam String ciId, @RequestParam String gradeId) {
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			obj.setMessage(paiKeZuHeService.getGradeWeek(new ObjectId(gradeId),new ObjectId(ciId),getSchoolId()));
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage(0);
		}
		return obj;
	}

	@RequestMapping("/getZuHeConflictedSettledJXB")
	@ResponseBody
	public RespObj getZuHeConflictedSettledJXB(@RequestParam String zuHeId,@RequestParam String xqid) {
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			obj.setMessage(paiKeZuHeService.getZuHeConflictedSettledJXB(new ObjectId(zuHeId), new ObjectId(xqid), getSchoolId()));
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage(0);
		}
		return obj;
	}

	/**
	 * 查询组合中的教学班以及已排课时
	 * @param zuHeId
	 * @param xqid
	 * @return
	 */
	@RequestMapping("/getZuHeJXBList")
	@ResponseBody
	public RespObj getZuHeJXBList(@RequestParam String zuHeId,@RequestParam String xqid,@RequestParam String gradeId,@RequestParam Integer type) {
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			List<N33_JXBDTO> n33_jxbdtos = paiKeZuHeService.getZuHeJXBList(new ObjectId(zuHeId),new ObjectId(xqid),getSchoolId(),new ObjectId(gradeId),type);
			obj.setMessage(n33_jxbdtos);
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}

	/**
	 * 按照组合排课
	 * @param zuHeId
	 * @param xqid
	 * @return
	 */
	@RequestMapping("/saveKeBiaoByGroup")
	@ResponseBody
	public RespObj saveKeBiaoByGroup(@RequestParam String zuHeId,@RequestParam String xqid,@RequestParam String gradeId,@RequestParam Integer type,@RequestParam Integer x,@RequestParam Integer y) {
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			paiKeZuHeService.saveKeBiaoByGroup(new ObjectId(zuHeId),new ObjectId(xqid),x,y,getSchoolId(),new ObjectId(gradeId),type);
			obj.setMessage("放入成功");
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}

	/**
	 * 撤销
	 * @param zuHeId
	 * @param xqid
	 * @return
	 */
	@RequestMapping("/clearKeBiaoByGroup")
	@ResponseBody
	public RespObj clearKeBiaoByGroup(@RequestParam String zuHeId,@RequestParam String xqid,@RequestParam String gradeId,@RequestParam Integer x,@RequestParam Integer y) {
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			paiKeZuHeService.clearKeBiaoByGroup(new ObjectId(zuHeId), new ObjectId(xqid), x, y, getSchoolId(), new ObjectId(gradeId));
			obj.setMessage("撤销成功");
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}

	/**
	 * 得到组合的教学班
	 * @param zuHeId
	 * @param xqid
	 * @return
	 */
	@RequestMapping("/getKbByGroup")
	@ResponseBody
	public RespObj getKbByGroup(@RequestParam String zuHeId,@RequestParam String xqid,@RequestParam String gradeId) {
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			obj.setMessage(paiKeZuHeService.getKbByGroup(new ObjectId(zuHeId),new ObjectId(xqid),getSchoolId(),new ObjectId(gradeId)));
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}

	/**
	 * 查询组合排在某一个格子里的教学班
	 * @param zuHeId
	 * @param xqid
	 * @return
	 */
	@RequestMapping("/getXYJXBList")
	@ResponseBody
	public RespObj getXYJXBList(@RequestParam String zuHeId,@RequestParam String xqid,@RequestParam String gradeId,@RequestParam Integer x,@RequestParam Integer y) {
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			obj.setMessage(paiKeZuHeService.getXYJXBList(new ObjectId(zuHeId),new ObjectId(xqid),getSchoolId(),new ObjectId(gradeId),x,y));
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}

	/**
	 *
	 * @param zuHeId
	 * @param xqid
	 * @return
	 */
	@RequestMapping("/getYkbEntries")
	@ResponseBody
	public RespObj getYkbEntries(@RequestParam String zuHeId,@RequestParam String xqid,@RequestParam String gradeId) {
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			obj.setMessage(paiKeZuHeService.getYkbEntries(new ObjectId(zuHeId),new ObjectId(xqid),getSchoolId(),new ObjectId(gradeId)));
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}

	/***
	 * 自动创建组合
	 * @param ciId
	 * @param gradeId
	 * @return
	 */
	@RequestMapping("/ziDongChuangJianZuHe")
	@ResponseBody
	public RespObj ziDongChuangJianZuHe(@RequestParam String ciId, @RequestParam String gradeId, @RequestParam Integer type, HttpServletRequest request) {
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			groupService.getAllNoCTJXBZuHe(getSchoolId(), new ObjectId(ciId), new ObjectId(gradeId),type,request);
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage(0);
		}
		return obj;
	}

	/**
	 * 撤销组合在某个格子中的某一个教学班
	 * @param zuHeId
	 * @param xqid
	 * @return
	 */
	@RequestMapping("/clearZuHeJXBByJXBID")
	@ResponseBody
	public RespObj clearZuHeJXBByJXBID(@RequestParam String zuHeId,@RequestParam String xqid,@RequestParam String gradeId,@RequestParam Integer x,@RequestParam Integer y,@RequestParam String jxbId) {
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			paiKeZuHeService.clearZuHeJXBByJXBID(new ObjectId(zuHeId),new ObjectId(xqid),getSchoolId(),new ObjectId(gradeId),x,y,new ObjectId(jxbId));
			obj.setMessage("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}

	/**
	 * 将某一个教学班排在组合的某一个位置上
	 * @param zuHeId
	 * @param xqid
	 * @return
	 */
	@RequestMapping("/saveZuHeJXBByJXBID")
	@ResponseBody
	public RespObj saveZuHeJXBByJXBID(@RequestParam String zuHeId,@RequestParam String xqid,@RequestParam String gradeId,@RequestParam Integer x,@RequestParam Integer y,@RequestParam String jxbId) {
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			paiKeZuHeService.saveZuHeJXBByJXBID(new ObjectId(zuHeId),new ObjectId(xqid),getSchoolId(),new ObjectId(gradeId),x,y,new ObjectId(jxbId));
			obj.setMessage("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}

	@ResponseBody
	@RequestMapping("/getStatus")
	public Map<String, Object> getStatus(HttpServletRequest request) {
		return groupService.getStatus(request);
	}

	@RequestMapping("/initZuHeList")
	@ResponseBody
	public RespObj initZuHeList(@RequestParam String ciId,@RequestParam String gradeId) {
		RespObj obj = new RespObj(Constant.SUCCESS_CODE);
		try {
			paiKeZuHeService.initZuHeList(new ObjectId(ciId),getSchoolId(),new ObjectId(gradeId),1);
			paiKeZuHeService.initZuHeList(new ObjectId(ciId),getSchoolId(),new ObjectId(gradeId),2);
			obj.setMessage("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			obj.setCode(Constant.FAILD_CODE);
			obj.setMessage("服务器正忙");
		}
		return obj;
	}
}
