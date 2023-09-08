<template>
  <div>
    <h3 class="semesterName">{{ this.semesterName }}</h3>
    <div v-for="course in semester.courses" :key="course.courseId">
      <div>
        {{ course.coursePrefix }} {{ course.courseNumber }}:
        {{ course.courseName }}
      </div>
    </div>
    <div>Total Hours: {{ semester.totalHours }}</div>
  </div>
</template>

<script>
export default {
  name: "semester",
  props: {
    semester: Object,
  },
  data() {
    return {
      semesterName: "",
    };
  },
  methods: {
    convertToSemesterName(semesterId) {
      // Calculate the current year and month
      const currentDate = new Date();
      const currentYear = currentDate.getFullYear();
      const currentMonth = currentDate.getMonth();

      // Define your semester start and end months
      const fallStartMonth = 7;
      const springStartMonth = 0;

      // Calculate the year and semester progress

      let semesterProgress = "";
      if (semesterId % 2 == 0) {
        if (currentMonth >= fallStartMonth) {
          semesterProgress = "Fall";
        } else if (currentMonth >= springStartMonth) {
          semesterProgress = "Spring";
        } else {
          semesterProgress = "Fall";
        }
      } else {
        if (currentMonth >= fallStartMonth) {
          semesterProgress = "Spring";
        } else if (currentMonth >= springStartMonth) {
          semesterProgress = "Fall";
        } else {
          semesterProgress = "Spring";
        }
      }

      const yearProgress = Math.floor(semesterId / 2);

      // Create the semester name
      const semesterNameConverted = `${semesterProgress} ${currentYear + yearProgress}`;

      this.semesterName = semesterNameConverted;
    },
  },
  created() {
    this.convertToSemesterName(this.semester.semesterId);
  },
};
</script>

<style>
.semesterName {
    text-align: center;
}
</style>
