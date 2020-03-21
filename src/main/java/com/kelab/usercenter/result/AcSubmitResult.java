package com.kelab.usercenter.result;

public class AcSubmitResult {

    private Integer ac;

    private Integer submit;

    public AcSubmitResult() {
    }

    public AcSubmitResult(Integer ac, Integer submit) {
        this.ac = ac;
        this.submit = submit;
    }

    public Integer getAc() {
        return ac;
    }

    public void setAc(Integer ac) {
        this.ac = ac;
    }

    public Integer getSubmit() {
        return submit;
    }

    public void setSubmit(Integer submit) {
        this.submit = submit;
    }
}
