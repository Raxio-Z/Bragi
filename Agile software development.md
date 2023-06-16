# gitlab 敏捷开发文档

## 开发流程

### 工作流简介

采用敏捷开发的基本工作流程，利用issue进行开发活动的管理。

- 首先，**开发一个新功能前必须先撰写issue**，内容采用feature模板，并对issue设置合理的Label (模块归属，讨论，ToDo等) 和计划的Due date（参考Milestone，可不完全一致）
- issue划分的参考准则是在较短的时间内<4周可完成的功能，较大的issue作为Epic或者进行拆分
- 其次，每周讨论将合理的issue分配到Doing并由**Maintainer**分配developer处理该issue
- Maintainer或者一名负责该issue的developer从该issue创建merge request或创建branch，并选择正确的目标分支 (develop)
  - merge request 会直接创建一个WIP merge request，并且创建一个该issue的远端分支，WIP表示该merge request还未完成
  - create branch 会创建一个该issue的远端分支
- 所有处理该issue的developer在本地创建该issue的远端分支的跟踪分支，直接在该分支上进行工作；或者继续创建该跟踪分支的子分支，完成子分支工作后merge到该跟踪分支上
- 完成工作后，**maintainer负责处理该issue的merge request**

### 工作流示意

1. 克隆项目（**我们由github迁移的就不用克隆了**）:

   ```bash
   git clone https://gitlab.com/xxx.git
   ```

2. 由组长峥神创建本周issue

   

   ![alt](https://github.com/Raxio-Z/Bragi/blob/main/images/image-20230422184013706.png?inline=false)

   **重点选项：**

   ![image-20230422215841065](https://github.com/Raxio-Z/Bragi/blob/main/images/image-20230422215841065.png?inline=false)

   本周我先代峥神创建了，创建好的issue（组员还没有全加进来，所以issue处理人暂时都写峥神）如下：

   ![image-20230422202656494](https://github.com/Raxio-Z/Bragi/blob/main/images/image-20230422202656494.png?inline=false)

3. 按照任务分配，每人进入issue创建对应的issue分支（如果多个人对应一个issue，也只需要创建一个issue分支），以笔记引用 issue 举例：

   ![image-20230422213032901](https://github.com/Raxio-Z/Bragi/blob/main/images/image-20230422213032901.png?inline=false)

4. 切换到所创建的模块分支

   ```bash
   git fetch origin
   git checkout -b 8-noteref origin/8-noteref
   ```

5. 写代码， 提交(`commit`)修改:

   ```bash
   git status # 查看变动文件
   git add . # 添加所有修改的文件到暂存区
   git commit -am "功能实现了一点了"
   git commit -am "功能又实现了一点了"
   git commit -am "功能快实现了"
   git commit -am "功能实现了"
   ```

6. 解决与上游最新版本代码的冲突，并推送(push)分支到 GitLab:

   ```bash
   git pull # 可能要处理conflict
   # 这里pull针对的是当前的issue分支，即多人合作处理一个issue时，该分支可能被多人改动，故提交前需要先pull远程分支
   git push origin $branch_name
   ```

7. 在GitLab页面上查看你的提交， 确认无误后点击网页上的按钮创建一个 **merge request .**

   ![image-20230422214226809](https://github.com/Raxio-Z/Bragi/blob/main/images/image-20230422214226809.png?inline=false)

8. 团队中负责上游分支的同学查看提交的代码，并将他们合并到上游分支或主分支.

9. issue完成后的close

   ![image-20230422214508885](https://github.com/Raxio-Z/Bragi/blob/main/images/image-20230422214508885.png?inline=false)



## 贡献分析

### commit

在`Repository - Contributor statistics`可以看到每个人的commit时间图：

![image-20230422215024863](https://github.com/Raxio-Z/Bragi/blob/main/images/image-20230422215024863.png?inline=false)



### Burndown chart

在`Issues - Milestones`处可以看到每个milestone的燃尽图（根据issue自动生成的，但是需要vip版本，可以试用30天，我的想法是我们别一起开，一个接一个开）

![image-20230422215246171](https://github.com/Raxio-Z/Bragi/blob/main/images/image-20230422215246171.png?inline=false)



## Mapping Agile artifacts to GitLab features

![image-20230422221652705](https://gitlab.com/stackoverfloww1/Bragi/-/raw/dev/images/image-20230422221652705.png?inline=false)