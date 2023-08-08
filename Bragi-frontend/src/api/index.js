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

export const reqArticleGetArticleList = (config) =>
    requests({ url: "/article", method: "get",...config });

export const reqArticleGetText = (author, pageIndex, pageSize) =>
    requests({
    url: '/article/' + author + '?pageIndex=' + (pageIndex - 1) + '&pageSize=' + pageSize,
    method: "get"
  });

export const reqArticleGetCurText = (author, articleId) =>
    requests({
        url: "/article/" + author + "/" + articleId,
        method: "get"
    });

export const reqArticlePostArticle = (params,config) =>
    requests({ url: "/article", method: "post", data: params,...config});

export const reqArticlePostBatchDel = (params,config) =>
    requests({ url: "/article/batchDel", method: "post", data: params,...config });

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

// WARNING: DEPRECATED
export const reqFilePostUpload = (params,config) =>
    requests({ url: "/file", method: "post", data: params,...config });

export const reqFilePostVditor = (params) =>
    requests({ url: "/file/vditor", method: "post", data: params });

export const reqFileGetUploadedFiles = (params) =>
    requests({ url: "/file", method: "get", data: params });

export const reqFileLoad = (pageIndex,pageSize,config) =>
    requests({ url: '/file?pageIndex=' + pageIndex + '&pageSize=' + pageSize, method: "get", ...config});

export const reqFileDelBatch = (params, config) =>
    requests({ url: "/file/delete/batch", method: "post", data: params ,...config});

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

// WARNING: DEPRECATED
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

export const reqNotePostSearch = (params) =>
    requests({ url: "/note/search", method: "post", data: params });

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

// alias
export const reqNoteRefJump = (value, config) =>
    requests({ url: "/note/path/" + value, method: "get",...config});

export const reqNoteRefGet = (params, config) =>
    requests({ url: "/note/getnoteref", method: "post", data: params,...config});

export const reqNoteRefInsert = (params, config) =>
    requests({ url: "/note/refinsert", method: "put", data: params,...config});

export const reqNoteRefDel = (params, config) =>
    requests({ url: "/note/delnoteref", method: "post", data: params,...config});

export const reqNoteRefGraph = (config) =>
    requests({ url: "/note/refgraph", method: "get", ...config});



export const reqUserPostLogin = (params) =>
    requests({ url: "/user/login", method: "post", data: params});

export const reqUserRetrieve = (params) =>
    requests({ url: "/user/confirm_retrieve", method: "post", data: params});

export const reqUserRetrievePwd = (params) =>
    requests({ url: "/user/retrievepwd", method: "post", data: params});

export const reqUserPostValidate = (params, config) =>
    requests({ url: "/user/validate", method: "post", data: params, ...config});

export const reqUserPostRegister = (params) =>
    requests({ url: "/user/register", method: "post", data: params });

export const reqUserPostChangePassword = (params,config) =>
    requests({ url: "/user/changePass", method: "post", data: params, ...config});

export const reqUserPostLogout = (params, config) =>
    requests({ url: "/user/logout", data:params, method: "post",...config });

export const reqUserEmailSignUp = (params) =>
    requests({ url: "/user/emailsignup", data:params, method: "post" });

export const reqUserPutRemoteUrl = (params) =>
    requests({ url: "/user/remote", method: "put", data: params });

export const reqUserGetRemoteUrl = (config) =>
    requests({ url: "/user/remote", method: "get",...config});

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

export const reqUserGetEditorConfig = (config) =>
    requests({ url: "/user/editorConfig", method: "get",...config });

export const reqUserPostEditorConfig = (params,config) =>
    requests({ url: "/user/updateEditorConfig", method: "post", data: params,...config});
