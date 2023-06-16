<template>
  <div class="welcome">
    <div style="margin-top: 60px">
      <h1 class="text_h1">
        Your
        <span style="text-decoration: underline;">
          <span>
            <img class="icon" src="../assets/wikis-icon.webp">
          </span>
          wiki
      </span>
        ,
        <span style="text-decoration: underline;">
          <span>
            <img class="icon" src="../assets/docs-icon.webp">
          </span>
          docs,
      </span>
      </h1>
      <h1 class="text_h1">
        &
        <span >
          <span >
            <img class="icon" src="../assets/projects-icon.webp">
          </span>
          <span style="text-decoration: underline;">
            projects.
          </span>
      </span>
        <span>
          Together!
      </span>
      </h1>
    </div>
    <div>
      <h2 style="margin-top: 30px;margin-bottom: 0;font-family:'inter-var',sans-serif;">
        Bragi is the connected workspace where
      </h2>
      <h2 style="margin: 0 auto;font-family:'inter-var',sans-serif;">
        better, faster work happens.
      </h2>
    </div>
    
    <img src="../assets/home-hero.jpg" style="margin-top: 60px">

  </div>
</template>

<script>
export default {
  name: "Warmup"
}
</script>

<style>
.welcome {
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  align-self: center;
  justify-content: center;
  align-items: center;
  text-align: center;
}

.text_h1{
  font-size: 4.3rem;
  font-weight: 700;
  font-family:"inter-var",sans-serif;
  margin-top: 0;
  margin-bottom: 0;
}

.icon{
  inset: 0px;
  box-sizing: border-box;
  padding: 0px;
  border: none;
  margin: 0;
  width: 52px;
  height: 60px;
  object-position: center bottom;
}

</style>