<template>
  <div class="home">
    <h1>Unofficial Degree Plan</h1>
    <div class="semesters">
      <semester
        class="semester"
        v-for="semester in this.semesters"
        :key="semester.semesterId"
        :semester="semester"
      />
    </div>
    <div>
      <div>Completed Courses</div>
      <div v-for="course in completedCourses" :key="course.courseId">{{ course.courseName }}</div>
    </div>
  </div>
</template>

<script>
import courseService from "../services/CourseService";
import Semester from "../components/Semester";
export default {
  name: "home",
  components: {
    Semester,
  },
  props: {
    student: Object,
  },
  data() {
    return {
      userId: null,
      studentId: this.$route.params.id,
      semesters: [],
      completedCourses: []
    };
  },
  methods: {
    getDegreePlan() {
  courseService
    .getRecommendedCourseOrder(this.studentId)
    .then((response) => {
      // Check if the response contains both plannedSemesters and completedCourses
      if (response.data && response.data.plannedSemesters && response.data.completedCourses) {
        // Parse planned semesters and completed courses separately
        const plannedSemesters = response.data.plannedSemesters.map((semester) => {
          return {
            semesterId: semester.semesterId,
            courses: semester.courses.map((course) => {
              return {
                courseId: course.courseId,
                coursePrefix: course.coursePrefix,
                courseNumber: course.courseNumber,
                courseName: course.courseName,
                hours: course.hours,
              };
            }),
            totalHours: semester.totalHours,
          };
        });

        const completedCourses = response.data.completedCourses.map((course) => {
          return {
            courseId: course.courseId,
            coursePrefix: course.coursePrefix,
            courseNumber: course.courseNumber,
            courseName: course.courseName,
            hours: course.hours,
          };
        });

        // Update the semesters and completed courses data
        this.semesters = plannedSemesters;
        this.completedCourses = completedCourses;
      } else {
        // Handle the case where the response is missing data
        console.error("Invalid response data format");
      }
    })
    .catch((error) => {
      // Handle any errors here
      console.error(error);
    });
},

    getUserId() {
      this.userId = this.$store.state.user.userId;
    },
  },
  created() {
    this.getUserId();
    this.getDegreePlan();
  },
};
</script>
<style>
.semesters {
  width: 1000px;
  display: flex; /* Apply flex layout */
  flex-wrap: wrap; /* Allow wrapping to the next line */
  justify-content: space-between;
}
.semester {
  border: 2px solid #333;
  border-radius: 10px;
  background-color: #f9f9f9;
  width: 350px;
  height: auto;
  margin: 20px;
  bottom: 40px;
  padding: 20px;
  left: 10%;
  font-size: 1rem;
  text-decoration: none;
  color: black;
  transition: border 0.2s ease, transform 0.3s ease-in-out;
  box-shadow: 0px 5px 10px 0px rgba(0, 0, 0, 0.5);
}
h1 {
  text-align: center;
}
.home {
  display: flex;
  flex-direction: column;
  align-items: center;
}
</style>
