import axios from "axios";

export default {
  getStudents(id) {
    return axios.get("", {
      params: {
        id: id
      }
    });
  },
  async createStudent(student) {
    const response = await axios.post("", student);
    return response.data;
  },
};