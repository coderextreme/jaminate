"use strict";
var System = Java.type('java.lang.System')
var BufferedReader = Java.type('java.io.BufferedReader')
var InputStreamReader = Java.type('java.io.InputStreamReader')
var Collectors = Java.type('java.util.stream.Collectors')
load('X3Dautoclass.js')
let LENGTH = 10;


function doubleArrayToJavaFloatArray(d) {
    if (Float32Array)
	return Java.to(new Float32Array(d), Java.type("float[]"));
}

function booleanArrayToJava(b) {
	var JSBooleans = [];
	for (var i in b) {
		if (b[i] === "TRUE") {
			JSBooleans.push(true);
		} else if (b[i] === "FALSE") {
			JSBooleans.push(false);
		} else {
			JSBooleans.push(b[i]);
		}
	}
	return Java.to(JSBooleans, Java.type("boolean[]")); 
}

function makeChainingAnimation(json) {

	let chars = [];
	for (let ch in json) {
	      var X3D0 =  new X3D().setProfile("Immersive").setVersion("4.0")
	      .setHead(new head()
		.addComponent(new component().setName("HAnim").setLevel(1))
		.addMeta(new meta().setName("title").setContent("takes."+ch+".x3dv"))
		.addMeta(new meta().setName("creator").setContent("John Carlson, Joe Williams and Andreas Plesch"))
		.addMeta(new meta().setName("translator").setContent("X3DJSAIL"))
		.addMeta(new meta().setName("version").setContent("3.0"))
		.addMeta(new meta().setName("description").setContent("Create Chaining animation from a JSON file"))
		.addMeta(new meta().setName("generator").setContent("Jaminate, V3.0, https://github.com/coderextreme/jaminate/tree/main/Jaminate/app/src/main/resources/takesX3DJSAIL.js"))
		.addMeta(new meta().setName("license").setContent("https://github.com/coderextreme/jaminate/blob/main/LICENSE")))
	        var Scene0 = new Scene();
      	        X3D0.setScene(Scene0);
                Scene0.addChild(new WorldInfo().setTitle("takes."+ch+".x3dv"))




	      var X3D0_timers =  new X3D().setProfile("Immersive").setVersion("4.0")
	      .setHead(new head()
		.addComponent(new component().setName("HAnim").setLevel(1))
		.addMeta(new meta().setName("title").setContent("takes."+ch+".timers.x3dv"))
		.addMeta(new meta().setName("creator").setContent("John Carlson, Joe Williams and Andreas Plesch"))
		.addMeta(new meta().setName("translator").setContent("X3DJSAIL"))
		.addMeta(new meta().setName("version").setContent("3.0"))
		.addMeta(new meta().setName("description").setContent("Create Chaining animation from a JSON file"))
		.addMeta(new meta().setName("generator").setContent("Jaminate, V3.0, https://github.com/coderextreme/jaminate/tree/main/Jaminate/app/src/main/resources/takesX3DJSAIL.js"))
		.addMeta(new meta().setName("license").setContent("https://github.com/coderextreme/jaminate/blob/main/LICENSE")))
	        var Scene0_timers = new Scene();
      	        X3D0_timers.setScene(Scene0_timers);
                Scene0_timers.addChild(new WorldInfo().setTitle("takes."+ch+".timers.x3dv"))

		chars.push(ch);
		let chardata = json[ch];
		let charobj = {};
		charobj["timesperchar"] = chardata["TimesPerCharacter"];
		charobj["movesperchar"] = chardata["MovesPerCharacter"];
		function booleanTranslate(charmove) {
			let mpc = charobj["movesperchar"];
			let tpc = charobj["timesperchar"];
			let mpcl = mpc.length;
			let movebooleans = Array.from({length:mpcl}).map(x => "FALSE");
			for (let bi in movebooleans) {
				if (mpc[bi] === charmove) {
					movebooleans[bi] = "TRUE";
				}
			}
			return movebooleans;
		}

		function compressmoves(mpc, tpc) {
			let len = mpc.length;
			let newmpc = [];
			let newtpc = [];
			for (let mvi = 0; mvi < len; mvi++) {
				let charmove = mpc[mvi];
				let acm = mvi;
				let submove = acm;
				let appearingcharmove = charmove+submove;
				let tcm = tpc[acm];

				let nacm = (mvi + 1) % len;
				let ncharmove = mpc[nacm];
				let nsubmove = nacm;
				let nappearingcharmove = ncharmove+nsubmove;
				let ntcm = tpc[nacm];


				let pacm = (mvi - 1 + len) % len;
				let pcharmove = mpc[pacm];
				let psubmove = pacm;
				let pappearingcharmove = pcharmove+psubmove;
				let ptcm = tpc[pacm];
				if (charmove === pcharmove) {
					if (mvi === 0) {
						newmpc.push(charmove);
						newtpc.push(tcm);
					} else if (mvi === 1) {
					} else if (mvi === 2) {
					} else if (mvi < len - 1 && mvi > 0) {
						newtpc[newtpc.length-1] = tcm;
					} else if (mvi === len - 1) {
						newmpc.push(charmove);
						newtpc.push(tcm);
					} else {
						console.error("WTF?", mvi, charmove, pcharmove);
					}
				} else {
					newmpc.push(charmove);
					newtpc.push(tcm);
				}
			}
			charobj["movesperchar"] = newmpc;
			charobj["timesperchar"] = newtpc;
		}
		compressmoves(charobj["movesperchar"], charobj["timesperchar"]);

		let len = charobj["movesperchar"].length;
		for (let mvi = 0; mvi < len; mvi++) {
			let charmove = charobj["movesperchar"][mvi];
			let acm = mvi;
			let submove = acm;
			let appearingcharmove = charmove+submove;

			let nacm = (mvi + 1) % len;
			let ncharmove = charobj["movesperchar"][nacm];
			let nsubmove = nacm;
			let nappearingcharmove = ncharmove+nsubmove;

			let pacm = (mvi - 1 + len) % len;
			let pcharmove = charobj["movesperchar"][pacm];
			let psubmove = pacm;
			let pappearingcharmove = pcharmove+psubmove;

			let ctl = charobj["timesperchar"].length;
			let movebooleans = Array.from({length:ctl}).map(x => "FALSE");
			movebooleans[submove] = "TRUE";
			let cycleInterval = [];
			let current = [];
			let next = [];
			let prev = [];
			for (let cti = 0; cti < ctl; cti++) {
				current.push(charobj["timesperchar"][cti] * LENGTH);
				next.push(charobj["timesperchar"][(cti+1) % ctl] * LENGTH);
				if (movebooleans[cti] === "TRUE") {
					if (cti === ctl - 1) {
						cycleInterval.push(0.01);
					} else {
						cycleInterval.push(next[next.length-1] - current[current.length-1]);
					}
				} else {
					cycleInterval.push(0);
				}
			}
		        Scene0_timers.addComments(next[mvi] / LENGTH+" - "+current[mvi] / LENGTH+" = "+ cycleInterval[mvi] / LENGTH+", times "+LENGTH+" is "+cycleInterval[mvi]);
			Scene0_timers.addChild(new TimeSensor().setDEF(appearingcharmove+"Timer").setCycleInterval(cycleInterval[mvi]).setLoop(true).setEnabled(true));

			var bs = new BooleanSequencer().setDEF(appearingcharmove+"BooleanSequencer");
			if (typeof movebooleans !== 'undefined') {
				bs.setKey(doubleArrayToJavaFloatArray(charobj["timesperchar"]));
				bs.setKeyValue(booleanArrayToJava(movebooleans)); 
			}
			Scene0.addChild(bs);
			Scene0.addComments("[ "+charobj["movesperchar"].join(" ")+" ]"); 
			Scene0.addChild(new BooleanFilter().setDEF(appearingcharmove+"SPLITTERBooleanFilter"));
			Scene0.addChild(new BooleanFilter().setDEF(appearingcharmove+"FINALEBooleanFilter"));
			Scene0.addChild(new BooleanFilter().setDEF(appearingcharmove+"NEGATERBooleanFilter"));
			Scene0.addChild(new TimeTrigger().setDEF(appearingcharmove+"OffTimeTrigger"));
			Scene0.addChild(new TimeTrigger().setDEF(appearingcharmove+"OnTimeTrigger"));

			if (mvi === 0) {
				Scene0.addChild(new ROUTE().setFromNode(ch+"_z100sTimer").setFromField("isActive").setToNode(appearingcharmove+"SPLITTERBooleanFilter").setToField("set_boolean"));
			} else if (mvi === len - 1) {
				Scene0.addChild(new ROUTE().setFromNode(ch+"_z100sTimer").setFromField("cycleTime").setToNode(appearingcharmove+"Timer").setToField("set_stopTime"));
				Scene0.addChild(new ROUTE().setFromNode(ch+"_z100sTimer").setFromField("isActive").setToNode(appearingcharmove+"Timer").setToField("set_enabled"));
				Scene0.addChild(new TimeTrigger().setDEF(appearingcharmove+"FINALETimeTrigger"));
				Scene0.addChild(new ROUTE().setFromNode(appearingcharmove+"FINALEBooleanFilter").setFromField("inputFalse").setToNode(appearingcharmove+"FINALETimeTrigger").setToField("set_boolean"));
				Scene0.addChild(new ROUTE().setFromNode(appearingcharmove+"FINALETimeTrigger").setFromField("triggerTime").setToNode(appearingcharmove+"Timer").setToField("set_stopTime"));
			}
			Scene0.addChild(new ROUTE().setFromNode(ch+"_z100sTimer").setFromField("fraction_changed").setToNode(appearingcharmove+"BooleanSequencer").setToField("set_fraction"));
			Scene0.addChild(new ROUTE().setFromNode(appearingcharmove+"BooleanSequencer").setFromField("value_changed").setToNode(appearingcharmove+"SPLITTERBooleanFilter").setToField("set_boolean"));
			Scene0.addChild(new ROUTE().setFromNode(appearingcharmove+"SPLITTERBooleanFilter").setFromField("inputTrue").setToNode(appearingcharmove+"OnTimeTrigger").setToField("set_boolean"));
			Scene0.addChild(new ROUTE().setFromNode(appearingcharmove+"SPLITTERBooleanFilter").setFromField("inputFalse").setToNode(appearingcharmove+"OffTimeTrigger").setToField("set_boolean"));
			Scene0.addChild(new ROUTE().setFromNode(appearingcharmove+"SPLITTERBooleanFilter").setFromField("inputTrue").setToNode(appearingcharmove+"Timer").setToField("set_enabled"));
			Scene0.addChild(new ROUTE().setFromNode(appearingcharmove+"SPLITTERBooleanFilter").setFromField("inputFalse").setToNode(appearingcharmove+"Timer").setToField("set_enabled"));
			Scene0.addChild(new ROUTE().setFromNode(appearingcharmove+"OffTimeTrigger").setFromField("triggerTime").setToNode(appearingcharmove+"Timer").setToField("set_stopTime"));
			Scene0.addChild(new ROUTE().setFromNode(appearingcharmove+"OnTimeTrigger").setFromField("triggerTime").setToNode(appearingcharmove+"Timer").setToField("set_startTime"));
		}
		Scene0.addComments("###########################################################################################"); 
		for (let mvi = 0; mvi < len; mvi++) {
			let charmove = charobj["movesperchar"][mvi];
			let acm = mvi;
			let submove = acm;
			let appearingcharmove = charmove+submove;

			let nacm = (mvi + 1) % len;
			let ncharmove = charobj["movesperchar"][nacm];
			let nsubmove = nacm;
			let nappearingcharmove = ncharmove+nsubmove;

			let pacm = (mvi - 1 + len) % len;
			let pcharmove = charobj["movesperchar"][pacm];
			let psubmove = pacm;
			let pappearingcharmove = pcharmove+psubmove;

			Scene0.addChild(new ROUTE().setFromNode(appearingcharmove+"Timer").setFromField("stopTime").setToNode(appearingcharmove+"Timer").setToField("set_startTime"));
			Scene0.addChild(new ROUTE().setFromNode(appearingcharmove+"Timer").setFromField("isActive").setToNode(appearingcharmove+"NEGATERBooleanFilter").setToField("set_boolean"));
			Scene0.addChild(new ROUTE().setFromNode(appearingcharmove+"NEGATERBooleanFilter").setFromField("inputNegate").setToNode(appearingcharmove+"BooleanSequencer").setToField("next"));
		}
		console.log(X3D0.toFileClassicVRML("takes."+ch+".x3dv"));
		console.log(X3D0_timers.toFileClassicVRML("takes."+ch+".timers.x3dv"));
	}
}

var json = new BufferedReader(new InputStreamReader(System.in)).lines().collect(Collectors.joining("\n"));
var js = JSON.parse(json);
System.out.println(JSON.stringify(js, null, 2)); // send back to GUI
makeChainingAnimation(js);
