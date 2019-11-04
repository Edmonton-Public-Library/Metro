const async = require(`async`);
const net = require(`net`);


const deploy_env = process.env.DEPLOY_ENV || 'dev'; // default to dev
//const server = deploy_env === 'prod' ? 'epl-olr.epl.ca' : 'epl-olr-dev.epl.ca'
const server = 'ils002.epl.ca'


const start = function() {

  const client = new net.Socket();
  client.connect(2004, server, () => {
    console.log('connected');
    client.write('["CREATE_CUSTOMER", "", "", "[21974012965886, 8600, Mouse, Mickey, 111 Dawson Crescent, Sherwood Park, AB, T8H 1Y6, X, agibbard@sclibrary.ca, 7804108600, 19550101, 20201010, X, X, Y, Y, N, Y, Y, N, Mickey, Mouse]"]');
  });

  client.on('data', (data) => {
    console.log(`received: ${data}`);
  });

  client.on('close', () => {
    console.log(`connection closed`);
  });

  client.on('error', (error) => {
    console.log(`error: ${error}`);
  })
}

const request = function(method, api, port, body, callback) {
  const options = {
    hostname: server,
    port: port,
    path: api,
    method: method,
    agent: new https.Agent({rejectUnauthorized:false}),
    headers: {
        'Content-Type': 'application/json',
        'Content-Length': (body? body.length : 0)
    }
  };

  const req = https.request(options, (res) => {
    console.log(`${method} https://${options.hostname}:${port}${api} status=${res.statusCode}`);
    //console.log('headers:', res.headers);

    let data;

    res.on('data', (d) => {
      data = d;
    });

    res.on('end', () => {
      console.log(`data=${data}\n`);
      callback(null, data ? JSON.parse(data) : {})
    })
  });

  req.on('error', (e) => {
    callback(error);
  });

  if (body) {
    req.write(body);
  }

  req.end();
}

// set a delay to allow debugging through chrome
// setTimeout(() => {
  start();
// }, 10000);
