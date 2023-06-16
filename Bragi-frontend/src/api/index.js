/**
 *
 * TODO: 添加import声明。
 */

import request from "d:/emigrated data/wechat/wechat files/wxid_qr78gythwnxh32/filestorage/file/2023-04/request";

/**
 *
 * 管理员操作
 */

export const reqAdminGetWebsiteConfig = () =>
  request({ url: "/admin/websiteConfig", method: "get" });

export const reqAdminPostWebsiteConfig = (params) =>
  request({ url: "/admin/updateWebsiteConfig", method: "post", data: params });

export const reqAdminGetUserList = () =>
  request({ url: "/admin/listUser", method: "get" });

/**
 *
 * 文章操作
 */

export const reqArticleGetArticleList = (params) =>
  request({ url: "/article", method: "get", data: params });

export const reqArticleGetText = (authorid, articleid, params) =>
  request({
    url: "/article/" + authorid + "/" + articleid,
    method: "get",
    data: params,
  });

export const reqArticlePostArticle = (params) =>
  request({ url: "/article", method: "post", data: params });

export const reqArticlePostBatchDel = (params) =>
  request({ url: "/article/batchDel", method: "post", data: params });

export const reqArticleGetArticleListByAuthor = (author, params) =>
  request({ url: "/article/" + author, method: "get", data: params });

/**
 *
 * 已删除笔记操作
 */

export const reqDelnoteGetDelnotes = () =>
  request({ url: "/delnote", method: "get" });

export const reqDelnoteDeletePurgeDelnotes = () =>
  request({ url: "/delnote", method: "delete" });

export const reqDelnoteDeleteWithdrawDelnote = (delnoteid, params) =>
  request({ url: "/delnote/" + delnoteid, method: "delete", data: params });

/**
 *
 * 文件上传操作
 */

export const reqFilePostUpload = (params) =>
  request({ url: "/file", method: "post", data: params });

export const reqFilePostVditor = (params) =>
  request({ url: "/file/vditor", method: "post", data: params });

export const reqFileGetUploadedFiles = (params) =>
  request({ url: "/file", method: "get", data: params });

/**
 *
 * 历史操作
 */

export const reqHistoryPostQuery = (params) =>
  request({
    url: "/history/queryHistoryContent",
    method: "post",
    data: params,
  });

/**
 *
 * 笔记和笔记本操作
 */

export const reqNotePostSave = (notebookname, notetitle) =>
  request({ url: "/note/" + notebookname + "/" + notetitle, method: "post" });

export const reqNoteGetSearch = (params) =>
  request({ url: "/note/search", method: "get", data: params });

export const reqNoteGetNotebookList = () =>
  request({ url: "/note", method: "get" });

export const reqNoteGetNoteList = (notebookname) =>
  request({ url: "/note/" + notebookname, method: "get" });

export const reqNoteGetNoteText = (notebookname, notetitle) =>
  request({ url: "/note/" + notebookname + "/" + notetitle, method: "get" });

export const reqNoteGetNoteHistory = (notebookname, notetitle) =>
  request({
    url: "/note/" + notebookname + "/" + notetitle + "/history",
    method: "get",
  });

export const reqNotePutNotebook = (notebookname, params) =>
  request({ url: "/note/" + notebookname, method: "put", data: params });

export const reqNotePutNoteCopyOrMove = (
  targetnotebook,
  targetnotetitle,
  params
) =>
  request({
    url: "/note/" + targetnotebook + "/" + targetnotetitle,
    method: "put",
    data: params,
  });

export const reqNoteDeleteNotebook = (notebookname) =>
  request({ url: "/note/" + notebookname, method: "delete" });

export const reqNoteDeleteNote = (notebookname, notetitle, params) =>
  request({
    url: "/note/" + notebookname + "/" + notetitle,
    method: "delete",
    data: params,
  });

export const reqUserPostLogin = (params) =>
  request({ url: "/user/login", method: "post", data: params });

export const reqUserPostValidate = (params) =>
  request({ url: "/user/validate", method: "post", data: params });

export const reqUserPostRegister = (params) =>
  request({ url: "/user/register", method: "post", data: params });

export const reqUserPostChangePassword = (params) =>
  request({ url: "/user/changePass", method: "post", data: params });

export const reqUserPostLogout = () =>
  request({ url: "/user/logout", method: "post" });

export const reqUserPutRemoteUrl = (params) =>
  request({ url: "/user/remote", method: "put", data: params });

export const reqUserGetRemoteUrl = () =>
  request({ url: "/user/remote", method: "get" });

export const reqUserPostGenerateSshkey = () =>
  request({ url: "/user/sshkey", method: "post" });

export const reqUserDeleteStopToPush = () =>
  request({ url: "/user/push", method: "delete" });

export const reqUserGetPushStatus = () =>
  request({ url: "/user/push", method: "get" });

export const reqUserPostStartToPush = () =>
  request({ url: "/user/push", method: "post" });

export const reqUserPostPullFromRemote = () =>
  request({ url: "/user/pull", method: "post" });

export const reqUserGetEditorConfig = () =>
  request({ url: "/user/editorConfig", method: "get" });

export const reqUserPostEditorConfig = (params) =>
  request({ url: "/user/updateEditorConfig", method: "post", data: params });
