const async = require(`async`);
const net = require(`net`);


const deploy_env = process.env.DEPLOY_ENV || 'dev'; // default to dev
const server = deploy_env === 'prod' ? 'epl-olr.epl.ca' : 'epl-olr-dev.epl.ca'
//const server = 'ils002.epl.ca'

const token = '55u1dqzu4tfSk2V4u5PW6VTMqi9bzt2d'; // TODO: place your token here
const READY = 'XK0';
const HANG_UP = 'XX0';
const END = '\n';

const start = function() {

  const tasks = [];

  tasks.push((done) => {
    request({
      code: 'GET_STATUS',
      authorityToken: token,
      userId: 'dummy',
      pin: 'dummy',
      customer: 'null'}, done);
  });

  tasks.push((done) => {
    // const customer = {
    //    'ID': '21974012965886',
    //    'PIN': '8600',
    //    'PREFEREDNAME': 'Mouse,Mickey',
    //    'STREET': '111 Dawson Crescent',
    //    'CITY': 'Sherwood Park',
    //    'PROVINCE' : 'AB',
    //    'POSTALCODE': 'T8H 1Y6',
    //    'SEX': 'X',
    //    'EMAIL': 'agibbard@sclibrary.ca',
    //    'PHONE': '7804108600',
    //    'DOB': '19550101',
    //    'PRIVILEGE_EXPIRES': '20201010',
    //    'RESERVED': 'X',
    //    'ALTERNATE_ID': 'Y',
    //    'ISVALID': 'Y',
    //    'ISMINAGE': 'Y',
    //    'ISRECIPROCAL': 'N',
    //    'ISRESIDENT': 'Y',
    //    'ISGOODSTANDING': 'Y',
    //    'ISLOSTCARD ': 'N',
    //    'FIRSTNAME': 'Mickey',
    //    'LASTNAME': 'Mouse'
    // };
    const customer = {"ID":"21221012345678","PIN":"64058","PREFEREDNAME":"BILLY, Balzac","STREET":"7th Floor 10235 101 Street Nw","CITY":"Edmonton","PROVINCE":"AB","POSTALCODE":"H0P0H0","SEX":"M","EMAIL":"ilsadmins@epl.ca","PHONE":"780-496-4058","DOB":"19760823","PRIVILEGE_EXPIRES":"20201104","RESERVED":"User DELINQUENT","ALTERNATE_ID":"X","ISVALID":"Y","ISMINAGE":"Y","ISRECIPROCAL":"N","ISRESIDENT":"Y","ISGOODSTANDING":"Y","ISLOSTCARD":"N","FIRSTNAME":"Balzac","LASTNAME":"Billy"};

    request({
      code: 'CREATE_CUSTOMER',
      authorityToken: token,
      userId: 'null',
      pin: 'null',
      customer: JSON.stringify(customer)
    }, done);
  });

  tasks.push((done) => {
    request({
      code: 'GET_CUSTOMER',
      authorityToken: token,
      userId: '21221012345678',
      pin: '64058',
      customer: 'null'}, done);
  });

  async.series(tasks, (error, results) => {
    console.log(`Completed: error=${JSON.stringify(error)} results=${JSON.stringify(results)}`);
  });
}

const request = function(command, callback) {

  console.log(`\nREQUEST ${JSON.stringify(command)}\n`);

  const client = new net.Socket();
  client.connect(2004, server, () => {
    console.log(`Connecting to ${server}\n`);
  });

  client.on('data', (data) => {
    console.log(`Received: ${data}\n`);
    if (data.toString().startsWith(READY)) {
      client.write(`${JSON.stringify(command)}${END}`);
      client.write(`${HANG_UP}${END}`);
    }
  });

  client.on('close', () => {
    console.log(`Connection closed\n`);
    callback();
  });

  client.on('error', (error) => {
    console.log(`Error: ${error}\n`);
    callback(error);
  });
}

// set a delay to allow debugging through chrome
// setTimeout(() => {
  start();
// }, 10000);
