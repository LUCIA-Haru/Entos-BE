package com.lr.entos.infra.utils;

import com.lr.entos.shared.utils.message.SuccessMessageUtils;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
@UtilityClass
public class EntosLogUtils {
    public static String logStatusUpdate(String domain, UUID guid, boolean status) {
        String action = status ? "Activated" : "Deactivated";
        String strGuid = guid.toString();

        // Using your existing SuccessMessageUtils structure
        String successMessage = SuccessMessageUtils.SUCCESS_OPERATION_STATUS
                .formatted("✅ " + "UPDATED", domain + " Status", strGuid, action);

        log.info(successMessage);
        return successMessage;
    }

    public static String logUpdate(String domain, UUID guid, String object) {
        String strGuid = guid.toString();

        String successMessage = SuccessMessageUtils.SUCCESS_OPERATION_OBJ
                .formatted("✅ " + "UPDATED", object, domain + guid);

        log.info(successMessage);
        return successMessage;
    }
}
