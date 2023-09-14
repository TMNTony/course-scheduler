<template>
  <div class="home">
    <h1>Unofficial Degree Planner</h1>
    <div>My Students</div>
    <select name="students" id="students">
      <option
        v-for="student in students"
        :key="student.studentId"
        value="student.studentId"
      >
        {{ student.firstName }} {{ student.lastName }}
      </option>
    </select>
    <button @click="add">Add Student</button>
    <form v-if="adding" v-on:submit.prevent="createStudent()">
      <label for="firstName">First Name</label>
      <input
        type="text"
        name="firstName"
        id="firstName"
        v-model="newStudent.firstName"
      />
      <label for="lastName">Last Name</label>
      <input
        type="text"
        name="lastName"
        id="lastName"
        v-model="newStudent.lastName"
      />
      <label for="majorId">Major</label>
      <select name="majorId" id="majorId" v-model="majorId">
        <option
          v-for="major in majors"
          :key="major.majorId"
          :value="major.majorId"
        >
          {{ major.major }}
        </option>
      </select>

      <input type="submit" value="Create Student" />
    </form>
  </div>
</template>

<script>
import StudentService from "../services/StudentService";
import MajorService from "../services/MajorService";
import CombinedService from "../services/CombinedService";
export default {
  name: "home",
  components: {},
  data() {
    return {
      adding: false,
      userId: null,
      semesters: [],
      students: [],
      majors: [],
      newStudent: {
        studentId: null,
        firstName: "",
        lastName: "",
        majorId: null,
        advisorId: null,
      },
      majorId: null,
    };
  },
  methods: {
    getStudents() {
      StudentService.getStudents(this.userId)
        .then((response) => {
          this.students = response.data;
        })
        .catch((error) => {
          console.error(error);
        });
    },
    getUserId() {
      this.userId = this.$store.state.user.id;
    },
    getMajors() {
      MajorService.getMajors()
        .then((response) => {
          this.majors = response.data;
        })
        .catch((error) => {
          console.error(error);
        });
    },
    getCombinedData() {
      CombinedService.getCombinedData(this.userId)
        .then((response) => {
          this.students = response.data.students;
          this.majors = response.data.majors;
        })
        .catch((error) => {
          console.error(error);
        });
    },
    add() {
      this.adding = !this.adding;
    },
    createStudent() {
      this.newStudent.majorId = this.majorId;
      this.newStudent.advisorId = this.userId;

      StudentService.createStudent(this.newStudent)
      .then((response) => {

            const studentId = response;
            
            if (!isNaN(studentId)) {

                this.$router.push({
                    name: "schedule",
                    params: {
                        id: studentId
                    }
                });
            } else {
                console.error('Invalid student ID received:', response.data);
            }
        })
        .catch((error) => {
            console.error(error);
        });
    },
  },
  created() {
    this.getUserId();
    this.getCombinedData();
  },
};
</script>
<style>
h1 {
  text-align: center;
}
.home {
  display: flex;
  flex-direction: column;
  align-items: center;
}
</style>
