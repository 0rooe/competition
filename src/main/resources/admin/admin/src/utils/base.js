const base = {
    get() {
        return {
            url: window.location.protocol + "//" + window.location.host + "/springbootpx13e/",
            name: "springbootpx13e",
            // 退出到首页链接
            indexUrl: window.location.protocol + "//" + window.location.host + "/springbootpx13e/front/index.html"
        };
    },
    getProjectName() {
        return {
            projectName: "学科竞赛管理系统"
        }
    }
}
export default base
