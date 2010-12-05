function SystemNotification() {
}

SystemNotification.prototype.notificationEnabled = false;

SystemNotification.prototype.newCount = 0; //to keep track of multiple notifications events

SystemNotification.prototype.enableNotification = function () {
    this.notificationEnabled = true;
};

SystemNotification.prototype.disableNotification = function () {
    this.notificationEnabled = false;
};

SystemNotification.prototype.onBackground = function () {
    this.enableNotification();
};

SystemNotification.prototype.onForeground = function () {
    this.disableNotification();
};

SystemNotification.prototype.createStatusBarNotification = function (contentTitle, contentText, tickerText) {
    PhoneGap.exec(null, null, "systemNotification", "createStatusBarNotification", [contentTitle, contentText, tickerText]);
};

SystemNotification.prototype.updateNotification = function (contentText, tickerText, number) {
    this.newCount++;
    var contentTitle = "my title";
    if (this.newCount === 1) {
        this.createStatusBarNotification(contentTitle, contentText, tickerText);
    } else {
        PhoneGap.exec(null, null, "systemNotification", "updateNotification", [contentTitle, contentText, this.newCount]);
        this.showTickerText(tickerText);  //optional
    }
};

SystemNotification.prototype.cancelNotification = function (contentText) {
    this.newCount--;
    if (this.newCount === 0) {
        PhoneGap.exec(null, null, "systemNotification", "cancelNotification", []);
    }
    else {
	//updating the notification
        var contentTitle = "my title";
        PhoneGap.exec(null, null, "systemNotification", "updateNotification", [contentTitle, contentText, this.newCount]);
    }
};

SystemNotification.prototype.showTickerText = function (tickerText) {
    PhoneGap.exec(null, null, "systemNotification", "showTickerText", [tickerText]);
};

SystemNotification.prototype.touch = function () {
    PhoneGap.exec(null, null, "systemNotification", "touch", []);
};

PhoneGap.addConstructor(function () {
    if (typeof(navigator.systemNotification) == "undefined") {
        navigator.systemNotification = new SystemNotification();
        navigator.systemNotification.touch();  //this ensures that the plugin is added when phonegap kicks off
    }
});

