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
  data() {
    return {
      userId: null,
      semesters: [],
    };
  },
  methods: {
    getDegreePlan() {
      courseService
        .getRecommendedCourseOrder(this.userId)
        .then((response) => {
          // Parse the JSON response into JavaScript objects
          const data = response.data.map((semester) => {
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

          // Update the semesters data
          this.semesters = data;
        })
        .catch((error) => {
          // Handle any errors here
          console.error(error);
        });
    },
    getUserId(){
        this.userId = this.$store.state.user.userId;
    }
  },
  created() {
    this.getUserId();
    this.getDegreePlan();
    console.log(this.userId)
  },
};
</script>
<style>
.semesters {
  width: 1000px;
  display: flex; /* Apply flex layout */
  flex-wrap: wrap; /* Allow wrapping to the next line */
  justify-content: space-evenly
  
}
.semester {
  border: 2px solid #333;
  border-radius: 10px;
  background-color: #f9f9f9;
  width: 300px;
  height: auto;
  margin: 20px;
  display: grid;
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
