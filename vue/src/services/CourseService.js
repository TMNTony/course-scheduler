import axios from "axios";

export default {
  getRecommendedCourseOrder(id) {
    return axios.get(`/${id}/courses/recommended-order`);
  },
};
