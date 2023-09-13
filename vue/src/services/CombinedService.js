import axios from "axios";

export default {
  getCombinedData(id) {
    return axios.get("", {
        params: {
          id: id
        }
      });
    },
};