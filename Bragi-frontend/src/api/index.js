/**
 *
 * TODO: 添加import声明。
 */

import requests from "./require";
/**
 *
 * 管理员操作
 */

export const reqAdminGetWebsiteConfig = () =>
    requests({ url: "/admin/websiteConfig", method: "get" });

export const reqAdminPostWebsiteConfig = (params) =>
    requests({ url: "/admin/updateWebsiteConfig", method: "post", data: params });

export const reqAdminGetUserList = () =>
    requests({ url: "/admin/listUser", method: "get" });

/**
 *
 * 文章操作
 */

export const reqArticleGetArticleList = (params) =>
    requests({ url: "/article", method: "get", data: params });

export const reqArticleGetText = (authorid, articleid, params) =>
    requests({
    url: "/article/" + authorid + "/" + articleid,
    method: "get",
    data: params,
  });

export const reqArticlePostArticle = (params) =>
    requests({ url: "/article", method: "post", data: params });

export const reqArticlePostBatchDel = (params) =>
    requests({ url: "/article/batchDel", method: "post", data: params });

export const reqArticleGetArticleListByAuthor = (author, params) =>
    requests({ url: "/article/" + author, method: "get", data: params });

/**
 *
 * 已删除笔记操作
 */

export const reqDelnoteGetDelnotes = (config) =>
    requests({ url: "/delnote", method: "get",...config });

export const reqDelnoteDeletePurgeDelnotes = (config) =>
    requests({ url: "/delnote", method: "delete", ...config});

export const reqDelnoteDeleteWithdrawDelnote = (delnoteid,config) =>
    requests({ url: "/delnote/" + delnoteid, method: "delete", ...config});

export const reqDelnoteRecover = (delnoteid,config) =>
    requests({ url: "/delnote/" + delnoteid + "?recover=true", method: "delete", ...config});


/**
 *
 * 文件上传操作
 */

export const reqFilePostUpload = (params) =>
    requests({ url: "/file", method: "post", data: params });

export const reqFilePostVditor = (params) =>
    requests({ url: "/file/vditor", method: "post", data: params });

export const reqFileGetUploadedFiles = (params) =>
    requests({ url: "/file", method: "get", data: params });

/**
 *
 * 历史操作
 */

export const reqHistoryPostQuery = (params, config) =>
    requests({
    url: "/history/queryHistoryContent",
    method: "post",
    data: params,
    ...config
  });

export const reqHistoryRecover = (notebookname, notetitle,params,config) =>
    requests({ url: "/note/" + notebookname + "/" + notetitle, method: "post",data: params,...config });

/**
 *
 * 笔记和笔记本操作
 */

export const reqNotePostSave = (notebookname, notetitle,params,config) =>
    requests({ url: "/note/" + notebookname + "/" + notetitle, method: "post",data: params,...config });

export const reqNotePutNotebook = (notebookname, params, config) =>
    requests({ url: "/note/" + notebookname, method: "put", data: params , ...config});

export const reqRenameNotebook = (notebookname, params, config) =>
    requests({ url: "/note/" + notebookname, method: "put",data:params,...config });

export const reqRenameNote = (notebookname,notename, params, config) =>
    requests({ url: "/note/" + notebookname+"/"+notename, method: "put",data:params,...config });

export const reqNoteMove = (targetNotebookName,notename, params, config) =>
    requests({ url: "/note/" + targetNotebookName+"/"+notename, method: "put",data:params,...config });

export const reqNoteUpload = (params, config) =>
    requests({ url: "/note/upload", method: "post",data:params,...config });

export const reqNoteCopy = (targetNotebookName,notename, params, config) =>
    requests({ url: "/note/" + targetNotebookName+"/"+notename, method: "put",data:params,...config });

export const reqNoteGetSearch = (params) =>
    requests({ url: "/note/search", method: "get", data: params });

export const reqNoteGetNotebookList = (config) =>
    requests({ url: "/note", method: "get",...config });

export const reqNoteGetNoteList = (notebookname) =>
    requests({ url: "/note/" + notebookname, method: "get" });

export const reqNoteGetNoteText = (notebookname, notetitle, config) =>
    requests({ url: "/note/" + notebookname + "/" + notetitle, method: "get" ,...config});

export const reqNoteGetNoteHistory = (notebookname, notetitle, config) =>
    requests({
    url: "/note/" + notebookname + "/" + notetitle + "/history",
    method: "get",
    ...config
    });



export const reqNotePutNoteCopyOrMove = (
  targetnotebook,
  targetnotetitle,
  params
) =>
    requests({
    url: "/note/" + targetnotebook + "/" + targetnotetitle,
    method: "put",
    data: params,
  });

export const reqNoteDeleteNotebook = (notebookname) =>
    requests({ url: "/note/" + notebookname, method: "delete" });

export const reqNoteDeleteNote = (notebookname, notetitle, config) =>
    requests({
    url: "/note/" + notebookname + "/" + notetitle,
    method: "delete",
        ...config
  });

export const reqNoteAddTag = (params, config) =>
    requests({ url: "/note/tag", method: "post", data: params, ...config});

export const reqNoteGetTag = (notebookName,title, config) =>
    requests({ url: "/note/tag" + "?notebookName=" + notebookName + "&noteName=" + title, method: "get", ...config});

export const reqNoteDelTag = (notebookName,title,tagToDelete, config) =>
    requests({ url: "/note/tag" + "?notebookName=" + notebookName + "&noteName=" + title+ "&tagName=" +tagToDelete, method: "delete", ...config});



export const reqUserPostLogin = (params) =>
    requests({ url: "/user/login", method: "post", data: params });

export const reqUserPostValidate = (params) =>
    requests({ url: "/user/validate", method: "post", data: params });

export const reqUserPostRegister = (params) =>
    requests({ url: "/user/register", method: "post", data: params });

export const reqUserPostChangePassword = (params) =>
    requests({ url: "/user/changePass", method: "post", data: params });

export const reqUserPostLogout = (params, config) =>
    requests({ url: "/user/logout", data:params, method: "post",...config });

export const reqUserPutRemoteUrl = (params) =>
    requests({ url: "/user/remote", method: "put", data: params });

export const reqUserGetRemoteUrl = () =>
    requests({ url: "/user/remote", method: "get" });

export const reqUserPostGenerateSshkey = () =>
    requests({ url: "/user/sshkey", method: "post" });

export const reqUserDeleteStopToPush = () =>
    requests({ url: "/user/push", method: "delete" });

export const reqUserGetPushStatus = () =>
    requests({ url: "/user/push", method: "get" });

export const reqUserPostStartToPush = () =>
    requests({ url: "/user/push", method: "post" });

export const reqUserPostPullFromRemote = () =>
    requests({ url: "/user/pull", method: "post" });

export const reqUserGetEditorConfig = () =>
    requests({ url: "/user/editorConfig", method: "get" });

export const reqUserPostEditorConfig = (params) =>
    requests({ url: "/user/updateEditorConfig", method: "post", data: params });
