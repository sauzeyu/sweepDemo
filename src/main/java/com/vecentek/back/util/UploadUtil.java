package com.vecentek.back.util;

import com.vecentek.back.constant.ExcelConstant;
import com.vecentek.common.response.PageResp;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-05-17 16:57
 */
public class UploadUtil {
    /**
     * 校验文件是否为excel文件
     *
     * @param file 受检文件
     * @return 如果返回null，则校验通过
     */
    public static PageResp checkFile(MultipartFile file) {
        if (file == null) {
            return PageResp.fail("文件不能为空！");
        }
        String fileName = file.getOriginalFilename();

        if (StringUtils.isBlank(fileName)) {
            return PageResp.fail("文件名不能为空！");
        }

        if (!(fileName.endsWith(ExcelConstant.EXCEL_SUFFIX_XLS) || fileName.endsWith(ExcelConstant.EXCEL_SUFFIX_XLSX))) {
            return PageResp.fail("导入文件格式错误，请导入xlsx/xls格式的文件！");
        }
        return null;
    }

}
