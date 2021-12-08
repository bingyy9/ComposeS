package com.cisco.webex.teams.utils

// Note: Please use tags based on feature and not the fragment names
// And let's try to keep tag names alphabetized
// https://sqbu-github.cisco.com/WebExSquared/android-support/wiki/Logging-in-Android
object LoggingTags {
    const val ACCESSIBILITY_TAG = "Accessibility"
    const val ADAPTERS_TAG = "Adapters"
    const val ANDROID_LOGIN_TELEMETRY_TAG = "AndroidLoginTelemetry"
    const val APP_JOURNEY_TAG = "AppJourney"
    const val APP_UPDATER_TAG = "AppUpdater"
    const val APPLICATION_TAG = "WebexTeams"
    const val AUDIO_DEVICE_TAG = "AudioDevice"
    const val AUDIO_RECORDING_TAG = "AudioRecording"
    const val AUDIO_TAG = "Audio"
    const val AUTHENTICATION_HELPER_TAG = "AuthenticationHelper"
    const val AUTO_ANSWER_TAG = "AutoAnswer"
    const val AUTO_TAG = "AndroidAuto"
    const val AVATAR_DISPLAY_TYPE_TAG = "AvatarDisplayType"
    const val AVATAR_TAG = "Avatar"
    const val BACKTRACE_TAG = "BacktraceHandler"
    const val BROWSER_TAG = "Browser"
    const val CALENDAR_TAG = "Calendar"
    const val CALL_DIALPAD_TAG = "CallDialpad"
    const val CALL_HISTORY_TAG = "CallHistory"
    const val CALLING_ROSTER_TAG = "CallingRoster"
    const val CALL_PARTICIPANT_LIST_TAG = "CallParticipantList"
    const val CALLING_TAG = "Calling"
    const val NATIVE_CALL = "NativeCall"
    const val CAPTIONS_TAG = "Captions"
    const val CAMERA_TAG = "Camera"
    const val CHANNEL_ENFORCER_TAG = "ChannelEnforcer"
    const val CHROME_OS_DRAG_DROP_LISTENER_TAG = "ChromeOSDropListener"
    const val COBRANDING_TAG = "Cobranding"
    const val CONTACT_TAG = "Contact"
    const val CONTENT_PREVIEW_TAG = "ContentPreview"
    const val CONVERSATION_FILES_TAG = "ConversationFiles"
    const val CONVERSATION_INFO_TAG = "ConversationInfo"
    const val COVER_IMAGE_TAG = "CoverImageView"
    const val CREATE_SPACE_TAG = "CreateSpace"
    const val CUSTOM_TABS_TAG = "CustomTabs"
    const val DEVELOPER_OPTIONS_TAG = "DevOptions"
    const val DEVICE_CONTROL_TAG = "DeviceControl"
    const val DEVICE_TAG = "Device"
    const val DNS_SERVER_INFO = "DNSServerInfo"
    const val DYNAMIC_CHANNEL_TAG = "DynamicChannel" // Push Notification Dynamic Channels
    const val E_911_TAG = "E911"
    const val ECM_AUTH_TAG = "ECMAuth"
    const val ECM_TAG = "ECM"
    const val ECM_WEBVIEW_TAG = "ECMWebView"
    const val EMBEDDED_APPS_TAG = "EmbeddedApps"
    const val ESCALATION_INVITE_TAG = "EscalationInvite"
    const val FEEDBACK_TAG = "Feedback"
    const val FILE_COMPRESSION_CHECK_TAG = "FileCompressionCheck"
    const val FILE_PREVIEW_TAG = "FilePreview"
    const val FILE_PROCESS_TAG = "FileProcess"
    const val FILE_PROCESS_TRACK_TAG = "FileProcessTrack"
    const val FILE_PRUNE_TAG = "FilePrune"
    const val GOOGLE_ASSISTANT_TAG = "GoogleAssistant"
    const val GRID_VIDEO_TAG = "GridVideo"
    const val IMAGE_TAG = "Image"
    const val IN_CALL_ANNOTATION_TAG = "InCallAnnotation"
    const val JOIN_MEETING = "JoinMeeting"
    const val KEYSTORE_TAG = "KeyStore"
    const val LINKS_TAG = "Links"
    const val LOCAL_PREVIEW_TAG = "LocalPreview"
    const val LOCALIZED_STRING_RESOURCE_TAG = "LocalizedString"
    const val LOGIN_TAG = "Login"
    const val MEETING_CONTAINER_TAG = "MeetingContainer"
    const val MEETING_HOST_TAG = "MeetingHost"
    const val MEETING_NOTIFICATIONS_TAG = "MeetingNotifications"
    const val MEETINGS_TAG = "Meetings"
    const val MERCURY_TAG = "Mercury"
    const val MESSAGE_PINS_TAG = "MessagePins"
    const val MESSAGE_TAG = "Message"
    const val NATIVE_CONTACT_TAG = "NativeContact"
    const val NATIVE_MESSAGE_IMAGE_FETCH_TAG = "NativeImageFetch"
    const val NATIVE_MESSAGE_SCROLL_TAG = "NativeScroll"
    const val NATIVE_MESSAGE_SCROLL_TELEMETRY_TAG = "NativeScrollTelemetry"
    const val NATIVE_MESSAGE_TAG = "NativeMessage"
    const val NAVIGATION_TAG = "Navigation"
    const val NETWORK_TAG = "Network"
    const val NOTIFICATIONS_TAG = "Notifications"
    const val PAIRING_TAG = "Pairing"
    const val PARTICIPANT_FILM_STRIP_TAG = "ParticipantFilmStrip"
    const val PEOPLE_TAG = "People"
    const val PERMISSIONS_TAG = "PermissionsTAG"
    const val PER_USER_SETTINGS_TAG = "PerUserSettings"
    const val PHONE_SERVICE_NAVIGATION_TAG = "PhoneServiceNavigation"
    const val PHONE_SERVICE_PREFERENCE_TAG = "PhoneServicePreference"
    const val PHOTO_ADAPTER_GLIDE_REQUEST_TAG = "PhotoAdapterGlideRequest"
    const val PHOTO_ADAPTER_TAG = "PhotoAdapter"
    const val PHOTO_GALLERY_TAG = "PhotoGallery"
    const val PLAY_STORE_RATING_TAG = "PlayStoreRating"
    const val PREFERENCES_TAG = "Preferences"
    const val PRESENCE_TAG = "Presence"
    const val PROFILE_VIEW_TAG = "Profile"
    const val PUSH_NOTIFICATION_TAG = "PushNotification"
    const val PUSH_TELEMETRY_TAG = "PushTelemetry"
    const val PUSH_REGISTRATION_TAG = "PushRegistration"
    const val SUSPEND_TAG = "Suspend"
    const val RECORDINGS_TAG = "Recordings"
    const val RINGTONE_TAG = "Ringtone"
    const val SCREEN_PROXIMITY_TAG = "ScreenProximity"
    const val SCREEN_SHARE_TAG = "ScreenShare"
    const val SEARCH_TAG = "Search"
    const val SECURITY_PROVIDER_TAG = "SecurityProvider"
    const val SEND_MESSAGE_TAG = "SendMessage"
    const val SERVICE_CHECKER_TAG = "ServiceChecker"
    const val SETTINGS_TAG = "Settings"
    const val SHARED_PREFS_TAG = "SharedPrefsUtils"
    const val SHARING_TAG = "Sharing"
    const val SIGN_OUT_TAG = "SignOut"
    const val SMART_REPLIES_TAG = "SmartReplies"
    const val SPACE_LIST_TAG = "SpaceList"
    const val SPACE_OPTION_TAG = "SpaceOption"
    const val SPACES_TAG = "Spaces"
    const val SYSTEM_INFO_TAG = "SystemInfo"
    const val TEAM_DETAILS_TAG = "TeamDetails"
    const val TEAMS_TAG = "Teams"
    const val TEST_TELEMETRY_TAG = "TestTelemetry"
    const val THEME_TAG = "Theme"
    const val TTI_TELEMETRY_TAG = "TTITelemetry"
    const val UC_CALLING_TAG = "UCCalling"
    const val UC_TAG = "UC"
    const val UI_UTILS_TAG = "UiUtils"
    const val UPDATE_FILE_SHARE_TAG = "UpdateFileShare"
    const val USER_GUIDANCE_TAG = "UserGuidance"
    const val USER_MANAGER_TAG = "UserManager"
    const val UTILS_TAG = "Utils"
    const val VIDEO_TAG = "Video"
    const val VOICE_CLIPS_TAG = "VoiceClips"
    const val VOICEMAIL_PLAYER_TAG = "VoicemailPlayer"
    const val VOICEMAIL_TAG = "Voicemail"
    const val SWITCH_NETWORK_TAG = "SwitchNetwork"
    const val WHATS_NEW_TAG = "WhatsNew"
    const val WHITEBOARD_TAG = "WhiteBoard"
}
// Note: Please use tags based on feature and not the fragment names
// And let's try to keep tag names alphabetized
// https://sqbu-github.cisco.com/WebExSquared/android-support/wiki/Logging-in-Android