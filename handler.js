//Importing required modules.
const { Storage } = require("@google-cloud/storage");
const path = require("path");

//Path to the key file.
const pathKey = path.resolve("./calmind-33a00-f819f31401f2.json");

//Handler function to get Cloud Storage metadata.
const getLinkHandler = async (request, h) => {
  const bucketName = "soundcalmind";
  let prefix = request.params.prefix;

  //Capitalize the first letter of the prefix.
  prefix = prefix.charAt(0).toUpperCase() + prefix.slice(1);

  //Get the object names from the bucket.
  try {
    const objectNames = await getObjectByNamePrefix(bucketName, prefix);
    return objectNames;
  } catch (err) {
    console.error(err);
    return h.response("Error retrieving objects").code(500);
  }
};
//Function to get object names from the bucket.
const getObjectByNamePrefix = async (bucketName, prefix) => {
  //Create a new storage instance.
  const storage = new Storage({
    keyFilename: pathKey,
  });
  const bucket = storage.bucket(bucketName);

  //Set the prefix.
  const options = {
    prefix: prefix,
    delimiter: "/",
  };

  //Get the objects from the bucket.
  const [files] = await bucket.getFiles(options);

  const objectInfo = [];

  //Get the metadata for each object.
  for (const file of files) {
    const [metadata] = await file.getMetadata({ custom: true });
    const customMetadata = metadata.metadata;

    //Get the link, title, and duration from the metadata.
    const link = file.metadata.mediaLink;
    const title = file.name;
    const duration = Number(customMetadata.duration);

    const object = { link, title, duration };
    objectInfo.push(object);
  }

  return objectInfo;
};

module.exports = getLinkHandler;
