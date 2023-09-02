<template>
  <div class="home">
    <h1>Home</h1>
    <p>You must be authenticated to see this</p>
  </div>
</template>

<script>
import courseService from ('../services/CourseService')
export default {
  name: "home",
  data() {
    return {
      userId: null;
    }
  },
  methods: {

  },
  created(){
this.userId = this.$store.state.getters.getCurrentUser;
  }

};
</script>
