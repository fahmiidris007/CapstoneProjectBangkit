const { Storage } = require("@google-cloud/storage");

const getLinkHandler = async (request, h) => {
  const bucketName = "soundcalmind";
  let prefix = request.params.prefix;
  prefix = prefix.charAt(0).toUpperCase() + prefix.slice(1);

  try {
    const objectNames = await getObjectByNamePrefix(bucketName, prefix);
    return objectNames;
  } catch (err) {
    console.error(err);
    return h.response("Error retrieving objects").code(500);
  }
};

const getObjectByNamePrefix = async (bucketName, prefix) => {
  const storage = new Storage({
    keyFilename: "../calmind-33a00-f819f31401f2.json",
  });
  const bucket = storage.bucket(bucketName);

  const options = {
    prefix: prefix,
    delimiter: "/",
  };

  const [files] = await bucket.getFiles(options);

  const objectLinks = files.map((file) => file.metadata.mediaLink);

  return objectLinks;
};

module.exports = getLinkHandler;
