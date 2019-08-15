/**
 * 验证js
 */


var intReg = new RegExp("^[0-9]*[1-9][0-9]*$");



/**
 * 是不是正整数
 */
function isInt(i)
{
	 return intReg.test(i);
}
