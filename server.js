const Hapi = require("@hapi/hapi");
const routes = require("./routes");

//Function to initialize the server.
const init = async () => {
  //Create a new server instance.
  const server = Hapi.server({
    port: 8080,
  });

  server.route(routes);

  await server.start();
  console.log("Server running on", server.info.uri);
};

//Process unhandled promise rejections and exit the process.
process.on("unhandledRejection", (err) => {
  console.error("Unhandled promise rejection:", err);
  process.exit(1);
});

init();
