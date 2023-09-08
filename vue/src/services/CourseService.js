import axios from "axios";

export default {
  getRecommendedCourseOrder(id) {
    return axios.get(`/courses/${id}/recommended-order`);
  },
};
