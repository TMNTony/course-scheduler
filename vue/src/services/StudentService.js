import axios from "axios";

export default {
  getStudents(id) {
    return axios.get("", {
      params: {
        id: id
      }
    });
  },
  createStudent(student) {
    return axios.post("", student);
  },
};