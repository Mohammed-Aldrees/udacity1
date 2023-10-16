package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;

@Service
@AllArgsConstructor
public class CredentialService {
    private final EncryptionService encryptionService;
    private final CredentialMapper credentialMapper;

    public void addCredential(Credential credential, int userId) {
        credential.setKey(generateKey());
        credential.setPassword(encryptionService.encryptValue(credential.getPassword(), credential.getKey()));
        credential.setUserid(userId);

        credentialMapper.insertCredentials(credential);
    }

    private String generateKey() {
        final String CHARACTER_SET = "0123456789abcdefghijklmnopqrstuvwxyz";
        int keyLength = 16;
        SecureRandom random = new SecureRandom();

        StringBuffer buff = new StringBuffer(keyLength);
        for (int i = 0; i < keyLength; i++) {
            int offset = random.nextInt(CHARACTER_SET.length());
            buff.append(CHARACTER_SET.charAt(offset));
        }
        return buff.toString();
    }

    public void updateCredential(Credential newCredential) {
        //regenerate key
        newCredential.setKey(generateKey());
        newCredential.setPassword(encryptionService.encryptValue(newCredential.getPassword(), newCredential.getKey()));
        credentialMapper.updateCredential(newCredential);
    }

    public void deleteCredential(Integer credentialId) {
        credentialMapper.deleteCredential(credentialId);
    }

    public List<Credential> getCredentialList(Integer userId) {
        List<Credential> credList = credentialMapper.getAllCredentials(userId);

        for (Credential cred : credList) {
            cred.setPassword(encryptionService.decryptValue(cred.getPassword(), cred.getKey()));
        }

        return credList;
    }

    public Credential getCredentialById(Integer credentialId) {
        return credentialMapper.getCredentials(credentialId);
    }
}
