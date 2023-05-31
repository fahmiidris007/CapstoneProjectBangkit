const getLinkHandler = require("./handler");

const routes = [
  {
    method: "GET",
    path: "/objects/{prefix}",
    handler: getLinkHandler,
  },
];

module.exports = routes;
