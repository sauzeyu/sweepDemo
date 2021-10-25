export default {
  'POST /api/uploadFotaFile': (req, res) => {
    res.send({
      success: true,
      fileUrl: 'http://sad.demo.com/ashdjk/akqw.file',
      fileInfoId: Date.now(),
    });
  },
};
