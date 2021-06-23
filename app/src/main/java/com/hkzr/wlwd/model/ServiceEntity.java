package com.hkzr.wlwd.model;

/**
 * Created by born on 2017/5/12.
 */


/// <summary>
/// 授权信息
/// </summary>
/// <remarks>
/// 授权引导服务地址是固定的，为 http://yun.5lsoft.com/service/mobile.ashx
/// 每次登录后，都要缓存授权信息，如果引导服务出现故障，无法使用，则继续使用上次的授权信息
public class ServiceEntity {

    /// <summary>
    /// 服务号
    /// </summary>
    private String AuthCode;
    /// <summary>
    /// 客户名称
    /// </summary>
    private String CustName;
    /// <summary>
    /// 主界面显示标题
    /// </summary>
    private String AppTitle;
    /// <summary>
    /// 最新版本号 格式:1.6.2
    /// </summary>
    /// <remarks>如果返回版本高于当前版本，提示客户升级，安卓程序的下载地址为DownUrl字段</remarks>
    private String Version;
    /// <summary>
    /// 授权开始日期 格式:yyyyMMddHHmmss
    /// </summary>
    private String StartDate;
    /// <summary>
    /// 授权结束日期 格式:yyyyMMddHHmmss
    /// </summary>
    private String EndDate;
    /// <summary>
    /// 最终客户服务器服务地址
    /// </summary>
    private String TargetUrl;
    /// <summary>
    /// 登录背景URL
    /// </summary>
    /// <remarks>第一次登录后，要把登录背景图片缓存到本地，如果检测到URL有变化就需要下载新的图片</remarks>
    private String BgFileUrl;
    /// <summary>
    /// 安卓新版本程序下载地址
    /// </summary>
    private String DownUrl;
    /// <summary>
    /// 授权状态[1|0]
    /// </summary>
    /// <remarks>1为生效，其它为不生效</remarks>
    private String State;
    private String  AppRoot;

    public String getAppRoot() {
        return AppRoot;
    }

    public void setAppRoot(String appRoot) {
        AppRoot = appRoot;
    }

    public void setAuthCode(String AuthCode) {
        this.AuthCode = AuthCode;
    }

    public String getAuthCode() {
        return this.AuthCode;
    }

    public void setCustName(String CustName) {
        this.CustName = CustName;
    }

    public String getCustName() {
        return this.CustName;
    }

    public void setAppTitle(String AppTitle) {
        this.AppTitle = AppTitle;
    }

    public String getAppTitle() {
        return this.AppTitle;
    }

    public void setVersion(String Version) {
        this.Version = Version;
    }

    public String getVersion() {
        return this.Version;
    }

    public void setStartDate(String StartDate) {
        this.StartDate = StartDate;
    }

    public String getStartDate() {
        return this.StartDate;
    }

    public void setEndDate(String EndDate) {
        this.EndDate = EndDate;
    }

    public String getEndDate() {
        return this.EndDate;
    }

    public void setTargetUrl(String TargetUrl) {
        this.TargetUrl = TargetUrl;
    }

    public String getTargetUrl() {
        return this.TargetUrl;
    }

    public void setBgFileUrl(String BgFileUrl) {
        this.BgFileUrl = BgFileUrl;
    }

    public String getBgFileUrl() {
        return this.BgFileUrl;
    }

    public void setDownUrl(String DownUrl) {
        this.DownUrl = DownUrl;
    }

    public String getDownUrl() {
        return this.DownUrl;
    }

    public void setState(String State) {
        this.State = State;
    }

    public String getState() {
        return this.State;
    }


}

