package com.vecentek.back.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

/**
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-01-17 15:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadDTO<T> {
    /**
     * 上传成功个数
     */
    private Integer successfulNum;
    /**
     * 上传失败个数
     */
    private Integer errorNum;
    /**
     * 上传失败的数据清单
     */
    private List<T> errorList;


    public UploadDTO<T> buildUserVehicleDTO(int successfulNum, List<T> errorList) {
        this.setSuccessfulNum(successfulNum);
        this.setErrorList(errorList);
        if (Objects.nonNull(errorList)) {
            this.setErrorNum(errorList.size());
        } else {
            this.setErrorNum(0);
        }
        return this;
    }

    public UploadDTO<T> buildUserVehicleDTO(List<T> errorList, int successfulNum) {
        return this.buildUserVehicleDTO(successfulNum, errorList);
    }

}
