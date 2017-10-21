const functions = require('firebase-functions');
const admin = require('firebase-admin');
var Cryptr = require('cryptr'),
    cryptr = new Cryptr('myTotalySecretKey');
admin.initializeApp(functions.config().firebase);
const cors = require('cors')({origin: true});

exports.sendNotification = functions.database.ref('/Notice/0').onWrite((event) => {
    const data = event.data;
	const notice = data.val();
  	console.log(notice);
    console.log('Message received');
    if(!data.changed()){
        console.log('Nothing changed');
        return;
    }else{
        console.log(notice.title);
    }

    const payLoad = {
        notification:{
            title: 'Attention:'+notice.attention,
            body: notice.title,
            sound: "default"
        }
    };


    return admin.messaging().sendToTopic("IIITstudents", payLoad);
});
exports.cryptr = functions.https.onRequest((req, res) => {
 	var pass = req.query.pass
     cors(req, res, () => {
    res.send(cryptr.encrypt(pass));
   })
 
  	
});


exports.dcryptr = functions.https.onRequest((req, res) => {
  var epass = req.query.pass
       cors(req, res, () => {
    res.send(cryptr.dencrypt(epass));
   })
  	
});